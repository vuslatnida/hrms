package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.EmployerService;
import hrms.hrms.core.dataAccess.abstracts.WebMailDao;
import hrms.hrms.core.utilities.excelHelper.EmployerListExcelHelper;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.dataAccess.abstracts.EmployerDao;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.dataAccess.abstracts.SystemPersonnelDao;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.SystemPersonnel;
import hrms.hrms.entities.concretes.dtos.EmployerDto;

import hrms.hrms.entities.concretes.dtos.response.PhoneNoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @Override
    public DataResult<List<Employer>> getAllEmployers() {
        return new SuccessDataResult<List<Employer>>(employerDao.findAll(), "İş veren kişilerin bilgileri listelendi.");
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

            systemPersonnelDao.save(newSystemPersonnel);
            personDao.save(newPerson);
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
    public Result hrmsConfirm(EmployerDto employerDto) {
        return new SuccessResult(employerDto.getWebsiteMail() + " e-mail adresine doğrulama için e-mail gönderildi. HRMS personeli onayı bekleniyor. HRMS personeli tarafından onaylandı. Kişi listeye ekleniyor.");
    }

    @Override
    public Result exportToExcelEmployer(HttpServletResponse response) {
        try {
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String fileName = "employer-list-" + currentDateTime;

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
}
