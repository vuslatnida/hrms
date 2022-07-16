package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.EmployerService;
import hrms.hrms.core.dataAccess.abstracts.WebMailDao;
import hrms.hrms.core.entities.dtos.EmployerGetDto;
import hrms.hrms.core.utilities.excelHelper.EmployerListExcelHelper;
import hrms.hrms.core.utilities.pdfHelper.EmployerListPdfHelper;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.dataAccess.abstracts.EmployerDao;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.dtos.EmployerDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployerManager implements EmployerService {

    @Autowired
    private EmployerDao employerDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private WebMailDao webMailDao;

    private EmployerGetDto convertEntityToDto(Employer employer){
        EmployerGetDto newEmployerGetDto = new EmployerGetDto();
        newEmployerGetDto.setId(employer.getId());
        newEmployerGetDto.setFirstName(employer.getPerson().getFirstName());
        newEmployerGetDto.setLastName(employer.getPerson().getLastName());
        newEmployerGetDto.setJobposition(employer.getPerson().getJobposition());
        newEmployerGetDto.setPassword(employer.getPassword());
        newEmployerGetDto.setPhoneNo(employer.getPhoneNo());
        newEmployerGetDto.setWebsite(employer.getWebsite());
        newEmployerGetDto.setWebsiteMail(employer.getWebsiteMail());
        newEmployerGetDto.setCompanyName(employer.getCompanyName());

        return newEmployerGetDto;
    }

    @Override
    public  DataResult<List<EmployerGetDto>> getAllEmployers() {
        return new SuccessDataResult<List<EmployerGetDto>>(employerDao.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList()), "Bilgiler listelendi.");
    }

    @Override
    public DataResult<List<EmployerGetDto>> getAllPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of((pageNo-1),pageSize);

        if(employerDao.findAll(pageable).getContent().size() == 0){
            return new ErrorDataResult<List<EmployerGetDto>>("Kullanıcı bulunamadı.");
        }
        else{
            return new SuccessDataResult<List<EmployerGetDto>>(employerDao.findAll(pageable).getContent()
                    .stream()
                    .map(this::convertEntityToDto)
                    .collect(Collectors.toList()), "Bilgiler sayfa numarası ve sırasına göre getiriliyor.");
        }
    }

    @Override
    public Result addEmployer(EmployerDto employerDto) {
        if (webMailDao.existsByWebsiteMail(employerDto.getWebsiteMail()) || employerDao.existsByPhoneNo(employerDto.getPhoneNo())) {
            return new ErrorResult("Mail adresinizi veya telefon numaranızı kontrol ediniz. Kaydınız gerçekleştirilemedi.");
        }

        else {
            Employer newEmployer = new Employer();
            Person newPerson = new Person();

            newPerson.setJobposition(employerDto.getJobposition());
            newPerson.setFirstName(employerDto.getFirstName());
            newPerson.setLastName(employerDto.getLastName());

            newEmployer.setCompanyName(employerDto.getCompanyName());
            newEmployer.setPassword(employerDto.getPassword());
            newEmployer.setWebsite(employerDto.getWebsite());
            newEmployer.setPhoneNo(employerDto.getPhoneNo());
            newEmployer.setWebsiteMail(employerDto.getWebsiteMail());

            newEmployer.setPerson(newPerson);

            personDao.save(newEmployer.getPerson());
            employerDao.save(newEmployer);
            return new SuccessResult("Kişi listeye eklendi.");
        }
    }

    @Override
    public Result deleteEmployer(int id) {
        if(employerDao.existsById(id)){
            employerDao.deleteById(id);
            return new SuccessResult("Kişi listeden silindi.");
        }

        else{
            return new ErrorResult(id + " id numarasına ait kullanıcı bulunamadı.");
        }
    }

    @Override
    public DataResult<List<Employer>> getByCompanyNameContains(String companyName) {
        if (employerDao.existsByCompanyName(companyName)) {
            return new ErrorDataResult<List<Employer>>("Kullanıcı bulunamadı.");
        }

        else {
            return new SuccessDataResult<List<Employer>>(employerDao.getByCompanyNameContains(companyName), "Data listelendi.");
        }
    }

    @Override
    public DataResult<List<Employer>> getByWebsiteMailContains(String webMail) {
        if (employerDao.existsByWebsiteMail(webMail)) {
            return new ErrorDataResult<List<Employer>>("Kullanıcı bulunamadı.");
        }

        else {
            return new SuccessDataResult<List<Employer>>(employerDao.getByWebsiteMailContains(webMail), "Data listelendi.");
        }
    }

    @Override
    public Result updateEmployerPhoneNo(int id, String phoneNo) {
        if(employerDao.existsById(id)){
            if(employerDao.existsByPhoneNo(phoneNo)){
                return new ErrorResult("Bu telefon numarası bir başkasına aittir.");
            }

            else if (phoneNo.length() == 11) {
                Employer newEmployer = employerDao.findById(id);
                newEmployer.setPhoneNo(phoneNo);
                employerDao.save(newEmployer);
                return new SuccessResult("Kişinin telefon numarası güncellendi.");
            }

            else {
                return new ErrorResult("Lütfen telefon numaranızın karakter sayısına dikkat ediniz.");
            }
        }

        else {
            return new ErrorResult(id + " id numarasına ait bir kullanıcı bulunamadı.");
        }

    }

    @Override
    public Result updateEmployerPassword(int id, String password) {
        if(employerDao.existsById(id)){
            Employer newEmployer = employerDao.findById(id);
            newEmployer.setPassword(password);
            employerDao.save(newEmployer);
            return new SuccessResult("Kişinin şifresi güncellendi.");
        }

        else {
            return new ErrorResult(id + " id numarasına ait bir kullanıcı bulunamadı.");
        }
    }

    @Override
    public Result exportToExcelEmployer(HttpServletResponse response) {
        try {
            String fileName = "employer-list" ;

            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName+".xlsx");

            EmployerListExcelHelper employerListExcelHelper = new EmployerListExcelHelper(employerDao.findAll());
            employerListExcelHelper.export(response);
            return new SuccessResult(getAllEmployers().toString());
        }

        catch (Exception ex){
            return new ErrorResult("Bilinmeyen Bir Hata Oluştu");
        }
    }

    @Override
    public Result exportToPdfEmployer(HttpServletResponse response) {
        try {
            String fileName = "employer-list" ;

            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName+".pdf");

            EmployerListPdfHelper employerListPdfHelper = new EmployerListPdfHelper(employerDao.findAll());
            employerListPdfHelper.export(response);
            return new SuccessResult(getAllEmployers().toString());
        }

        catch (Exception ex){
            return new ErrorResult("Bilinmeyen Bir Hata Oluştu");
        }
    }
}
