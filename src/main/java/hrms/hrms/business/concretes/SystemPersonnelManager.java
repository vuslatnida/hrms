package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.SystemPersonnelService;
import hrms.hrms.core.dataAccess.abstracts.PositionDao;
import hrms.hrms.core.utilities.results.*;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.dataAccess.abstracts.SystemPersonnelDao;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.SystemPersonnel;
import hrms.hrms.entities.concretes.dtos.SystemPersonnelDto;
import hrms.hrms.entities.concretes.dtos.request.PositionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemPersonnelManager implements SystemPersonnelService {
    @Autowired
    private SystemPersonnelDao systemPersonnelDao;

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private PersonDao personDao;

    @Override
    public DataResult<List<SystemPersonnel>> getSystemPersonnel() {
        return new SuccessDataResult<List<SystemPersonnel>>(this.systemPersonnelDao.findAll(), "Sistem personeli bilgileri listelendi.");
    }

    @Override
    public Result addSystemPersonnel(SystemPersonnelDto systemPersonnelDto) {
        if(positionDao.existsByJobposition(systemPersonnelDto.getJobposition())){
            return new ErrorResult("Yazmış olduğunuz pozisyonda bir eleman bulunmaktadır. Lütfen farklı bir departmandan giriş yapmayı deneyiniz.");
        }
        else {
            SystemPersonnel newSystemPersonnel = new SystemPersonnel();
            Person newPerson = new Person();

            newPerson.setFirstName(systemPersonnelDto.getFirstName());
            newPerson.setLastName(systemPersonnelDto.getLastName());

            newSystemPersonnel.setJobposition(systemPersonnelDto.getJobposition());
            newSystemPersonnel.setPerson(newPerson);

            personDao.save(newPerson);
            systemPersonnelDao.save(newSystemPersonnel);
            return new SuccessResult("Kişi listeye eklendi.");
        }
    }

    @Override
    public Result deleteSystemPersonnel(int id, PositionDto positionDto) {
        if(systemPersonnelDao.existsByIdAndJobposition(id, positionDto.getJob_position())){
            systemPersonnelDao.deleteById(id);
            return new SuccessResult("Kişi listeden silindi.");
        }
        else{
            return new ErrorResult("Kişi listeden silinemedi.");
        }
    }

    @Override
    public DataResult<List<SystemPersonnel>> getByJobpositionContains(String jobPosition) {
        if (systemPersonnelDao.existsByJobposition(jobPosition)) {
            return new ErrorDataResult<List<SystemPersonnel>>("Kullanıcı bulunamadı.");
        }

        else {
            return new SuccessDataResult<List<SystemPersonnel>>(systemPersonnelDao.getByJobpositionContains(jobPosition), "Data listelendi.");
        }
    }
}
