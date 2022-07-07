package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.JobSeekersService;
import hrms.hrms.core.dataAccess.abstracts.IdentificationNoEmailDao;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.dataAccess.abstracts.JobSeekersDao;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.dataAccess.abstracts.SystemPersonnelDao;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.SystemPersonnel;
import hrms.hrms.entities.concretes.dtos.response.IdentificationNoDto;
import hrms.hrms.entities.concretes.dtos.response.IdentificationNoEmailDto;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekersManager implements JobSeekersService {

    @Autowired
    private JobSeekersDao jobSeekersDao;

    @Autowired
    private IdentificationNoEmailDao identificationNoEmailDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private SystemPersonnelDao systemPersonnelDao;

    @Override
    public DataResult<List<JobSeekers>> getAllJobSeekers() {
        return new SuccessDataResult<List<JobSeekers>>(jobSeekersDao.findAll(), "İş arayan kişilerin bilgileri listelendi");
    }

    @Override
    public Result addJobSeekers(JobSeekersDto jobSeekersDto) {
        if (identificationNoEmailDao.existsByIdentificationNo(jobSeekersDto.getIdentificationNo()) || identificationNoEmailDao.existsByEmail(jobSeekersDto.getEmail())) {
            return new ErrorResult("Kişinin e-mail veya TC kimlik numarasını tekrardan kontrol ediniz. Kişi listeye eklenemedi.");
        }
        else {
            JobSeekers newJobSeekers = new JobSeekers();
            Person newPerson = new Person();
            SystemPersonnel newSystemPersonnel = new SystemPersonnel();

            newSystemPersonnel.setJobposition(jobSeekersDto.getJobposition());

            newPerson.setFirstName(jobSeekersDto.getFirstName());
            newPerson.setLastName(jobSeekersDto.getLastName());

            newJobSeekers.setEmail(jobSeekersDto.getEmail());
            newJobSeekers.setBirthYear(jobSeekersDto.getBirthYear());
            newJobSeekers.setIdentificationNo(jobSeekersDto.getIdentificationNo());
            newJobSeekers.setPassword(jobSeekersDto.getPassword());

            newJobSeekers.setPerson(newPerson);
            newJobSeekers.setSystemPersonnel(newSystemPersonnel);

            systemPersonnelDao.save(newSystemPersonnel);
            personDao.save(newPerson);
            jobSeekersDao.save(newJobSeekers);
            return sendEmail(jobSeekersDto.getEmail());
        }
    }

    @Override
    public Result getByIdentificationNoAndEmail(IdentificationNoEmailDto identificationNoEmailDto){
        if(identificationNoEmailDao.existsByIdentificationNo(identificationNoEmailDto.getIdentificationNo()) || identificationNoEmailDao.existsByEmail(identificationNoEmailDto.getEmail())){
            return new SuccessResult("Bu TC kimlik numarası veya Email adresi bir başkasına aittir.");
        }
        else{
            return new ErrorResult("Bu TC kimlik numarası veya Email adresi kullanılmamaktadır.");
        }
    }

    @Override
    public Result sendEmail(String email) {
        if (identificationNoEmailDao.existsByEmail(email)){
            return new SuccessResult(email + " e-mail adresine doğrulama için e-mail gönderildi. Kayıt gerçekleştiriliyor.");
        }
        else{
            return new ErrorResult("e-mail doğrulaması gerçekleştirilemedi.");
        }
    }

    @Override
    public Result deleteJobSeeker(IdentificationNoDto identificationNoDto) {
        if(jobSeekersDao.existsByIdAndIdentificationNo(identificationNoDto.getId(), identificationNoDto.getIdentificationNo())){
            jobSeekersDao.deleteById(identificationNoDto.getId());
            return new SuccessResult("Kişi listeden silindi.");
        }
        else{
            return new ErrorResult("Kişi listeden silinemedi.");
        }
    }
}
