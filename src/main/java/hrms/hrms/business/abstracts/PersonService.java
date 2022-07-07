package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.entities.concretes.Person;

import java.util.List;

public interface PersonService {
    DataResult<List<Person>> getAllPersons();

    DataResult<List<Person>> getByFirstNameContains(String firstName);
}
