package hrms.hrms.dataAccess.abstracts;

import hrms.hrms.entities.concretes.JobSeekers;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JobSeekersDao extends JpaRepository<JobSeekers, Integer> {
    boolean existsByIdAndIdentificationNo(int id, String identificationNo);


}
