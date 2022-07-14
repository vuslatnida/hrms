package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.EmployerService;
import hrms.hrms.core.dataAccess.abstracts.WebMailDao;
import hrms.hrms.core.utilities.excelHelper.EmployerListExcelHelper;
import hrms.hrms.core.utilities.pdfHelper.EmployerListPdfHelper;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.dataAccess.abstracts.EmployerDao;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.dataAccess.abstracts.SystemPersonnelDao;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.SystemPersonnel;
import hrms.hrms.entities.concretes.dtos.EmployerDto;

import hrms.hrms.entities.concretes.dtos.request.PhoneNoDto;
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

    @Autowired
    private SystemPersonnelDao systemPersonnelDao;

    private EmployerDto convertEntityToDto(Employer employer){
        EmployerDto newEmployerDto = new EmployerDto();
        newEmployerDto.setFirstName(employer.getPerson().getFirstName());
        newEmployerDto.setLastName(employer.getPerson().getLastName());
        newEmployerDto.setJobposition(employer.getSystemPersonnel().getJobposition());
        newEmployerDto.setPassword(employer.getPassword());
        newEmployerDto.setPhoneNo(employer.getPhoneNo());
        newEmployerDto.setWebsite(employer.getWebsite());
        newEmployerDto.setWebsiteMail(employer.getWebsiteMail());
        newEmployerDto.setCompanyName(employer.getCompanyName());

        return newEmployerDto;
    }

    @Override
    public  DataResult<List<EmployerDto>> getAllEmployers() {
        return new SuccessDataResult<List<EmployerDto>>(employerDao.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList()), "Bilgiler listelendi.");
    }

    @Override
    public DataResult<List<EmployerDto>> getAllPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of((pageNo-1),pageSize);

        if(employerDao.findAll(pageable).getContent().size() == 0){
            return new ErrorDataResult<List<EmployerDto>>("Kullanıcı bulunamadı.");
        }
        else{
            return new SuccessDataResult<List<EmployerDto>>(employerDao.findAll(pageable).getContent()
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
            SystemPersonnel newSystemPersonnel = new SystemPersonnel();

            newSystemPersonnel.setJobposition(employerDto.getJobposition());

            newPerson.setFirstName(employerDto.getFirstName());
            newPerson.setLastName(employerDto.getLastName());

            newEmployer.setCompanyName(employerDto.getCompanyName());
            newEmployer.setPassword(employerDto.getPassword());
            newEmployer.setWebsite(employerDto.getWebsite());
            newEmployer.setPhoneNo(employerDto.getPhoneNo());
            newEmployer.setWebsiteMail(employerDto.getWebsiteMail());

            newEmployer.setPerson(newPerson);
            newEmployer.setSystemPersonnel(newSystemPersonnel);

            systemPersonnelDao.save(newEmployer.getSystemPersonnel());
            personDao.save(newEmployer.getPerson());
            employerDao.save(newEmployer);
            return hrmsConfirm(employerDto);
        }
    }

    @Override
    public Result deleteEmployer(PhoneNoDto phoneNoDto) {
        if(employerDao.existsByIdAndPhoneNo(phoneNoDto.getId(), phoneNoDto.getPhoneNo())){
            employerDao.deleteById(phoneNoDto.getId());
            return new SuccessResult("Kişi listeden silindi.");
        }

        else{
            return new ErrorResult("Kişi listeden silinemedi.");
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
    public Result sendWebEmail(EmployerDto employerDto) {
        if (webMailDao.existsByWebsiteMail(employerDto.getWebsiteMail())) {
            return hrmsConfirm(employerDto);
        }

        else {
            return new ErrorResult("e-mail doğrulaması gerçekleştirilemedi.");
        }
    }

    @Override
    public Result updateEmployerPhoneNo(int id, String phoneNo) {
        Employer newEmployer = employerDao.findById(id);
        newEmployer.setPhoneNo(phoneNo);
        employerDao.save(newEmployer);
        return new SuccessResult("Kişinin telefon numarası güncellendi.");
    }

    @Override
    public Result updateEmployerPassword(int id, String password) {
        Employer newEmployer = employerDao.findById(id);
        newEmployer.setPassword(password);
        employerDao.save(newEmployer);
        return new SuccessResult("Kişinin şifresi güncellendi.");
    }

    @Override
    public Result hrmsConfirm(EmployerDto employerDto) {
        return new SuccessResult(employerDto.getWebsiteMail() + " e-mail adresine doğrulama için e-mail gönderildi. HRMS personeli onayı bekleniyor. HRMS personeli tarafından onaylandı. Kişi listeye ekleniyor.");
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
