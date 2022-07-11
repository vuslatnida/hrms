package hrms.hrms.dataAccess.abstracts;

import hrms.hrms.entities.concretes.JobSeekers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSeekersDao extends JpaRepository<JobSeekers, Integer> {

    boolean existsByIdAndIdentificationNo(int id, String identificationNo);

    boolean existsByIdentificationNoContains(String identificationNo);

    JobSeekers findById(int id);

    List<JobSeekers> getByIdentificationNoContains(String identificationNo);
}
