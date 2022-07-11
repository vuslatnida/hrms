package hrms.hrms.dataAccess.abstracts;

import hrms.hrms.entities.concretes.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployerDao extends JpaRepository<Employer, Integer> {

    boolean existsByIdAndPhoneNo(int id, String phoneNo);

    boolean existsByPhoneNo(String phoneNumber);

    boolean existsByCompanyName(String companyName);

    boolean existsByWebsiteMail(String webMail);

    Employer findById(int id);

    List<Employer> getByCompanyNameContains(String companyName);

    List<Employer> getByWebsiteMailContains(String webMail);

}
