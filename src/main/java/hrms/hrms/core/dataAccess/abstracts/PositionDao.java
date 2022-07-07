package hrms.hrms.core.dataAccess.abstracts;

import hrms.hrms.entities.concretes.SystemPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionDao extends JpaRepository<SystemPersonnel, String> {
    boolean existsByJobposition(String jobposition);
}
