package hrms.hrms.dataAccess.abstracts;

import hrms.hrms.entities.concretes.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmployerDao extends JpaRepository<Employer, Integer> {
    boolean existsByIdAndPhoneNo(int id, String phoneNo);
}
