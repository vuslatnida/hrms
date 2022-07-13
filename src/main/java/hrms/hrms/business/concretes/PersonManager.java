package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.PersonService;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.ErrorDataResult;
import hrms.hrms.core.utilities.results.SuccessDataResult;
import hrms.hrms.dataAccess.abstracts.PersonDao;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.dtos.EmployerDto;
import hrms.hrms.entities.concretes.dtos.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonManager implements PersonService {

    @Autowired
    private PersonDao personDao;

    private PersonDto convertEntityToDto(Person person){
        PersonDto newPersonDto = new PersonDto();
        newPersonDto.setFirstName(person.getFirstName());
        newPersonDto.setLastName(person.getLastName());
        return newPersonDto;
    }

    @Override
    public DataResult<List<PersonDto>> getAllPersons() {
        return new SuccessDataResult<List<PersonDto>>(personDao.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList()), "Bilgiler listelendi.");
    }

    @Override
    public DataResult<List<Person>> getByFirstNameContains(String firstName) {
        if (personDao.existsByFirstName(firstName)) {
            return new ErrorDataResult<List<Person>>("Kullanıcı bulunamadı.");
        }

        else {
            return new SuccessDataResult<List<Person>>(personDao.getByFirstNameContains(firstName), "Data listelendi.");
        }
    }
}
