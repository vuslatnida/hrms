package hrms.hrms.core.dataAccess;

import hrms.hrms.entities.concretes.JobSeekers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationNoEmailDao extends JpaRepository<JobSeekers, String> {

    boolean existsByIdentificationNo(String identificationNo);

    boolean existsByEmail(String email);
}