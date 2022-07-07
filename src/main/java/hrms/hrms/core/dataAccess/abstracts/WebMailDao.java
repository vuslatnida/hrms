package hrms.hrms.core.dataAccess.abstracts;

import hrms.hrms.entities.concretes.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebMailDao extends JpaRepository<Employer, String> {
    boolean existsByWebsiteMail(String websiteMail);
}
