package hrms.hrms.dataAccess.abstracts;

import hrms.hrms.entities.concretes.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonDao extends JpaRepository<Person, Integer> {
    List<Person> getByFirstNameContains(String firstName);

    boolean existsByFirstName(String firstName);
}
