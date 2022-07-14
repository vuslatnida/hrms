package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.entities.concretes.Person;
import hrms.hrms.entities.concretes.dtos.PersonDto;

import java.util.List;

public interface PersonService {
    DataResult<List<PersonDto>> getAllPersons();
    DataResult <List<PersonDto>> getAllPage(int pageNo, int pageSize);
    DataResult<List<Person>> getByFirstNameContains(String firstName);
}
