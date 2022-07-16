package hrms.hrms.business.abstracts;

import hrms.hrms.core.entities.dtos.PersonGetDto;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.entities.concretes.Person;

import java.util.List;

public interface PersonService {

    DataResult<List<PersonGetDto>> getAllPersons();

    DataResult <List<PersonGetDto>> getAllPage(int pageNo, int pageSize);

    DataResult<List<Person>> getByFirstNameContains(String firstName);
}
