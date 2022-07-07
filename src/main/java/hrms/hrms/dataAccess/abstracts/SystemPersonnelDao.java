package hrms.hrms.dataAccess.abstracts;

import hrms.hrms.entities.concretes.SystemPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemPersonnelDao extends JpaRepository<SystemPersonnel, Integer> {
    boolean existsByIdAndJobposition(int id, String jobposition);
}
