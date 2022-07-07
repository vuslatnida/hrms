package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.PersonService;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.ErrorDataResult;
import hrms.hrms.core.utilities.results.SuccessDataResult;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.entities.concretes.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonManager implements PersonService {

    @Autowired
    private PersonDao personDao;

    @Override
    public DataResult<List<Person>> getAllPersons() {
        return new SuccessDataResult<List<Person>>(personDao.findAll() + "Person listesi getiriliyor.");
    }

    @Override
    public DataResult<List<Person>> getByFirstNameContains(String firstName) {
        if(personDao.existsByFirstName(firstName)){
            return new SuccessDataResult<List<Person>>(personDao.getByFirstNameContains(firstName),"Data listelendi.");

        }
        else {
            return  new ErrorDataResult<List<Person>>("Kullanıcı bulunamadı.");
        }
    }
}
