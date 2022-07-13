package hrms.hrms.core.login.dataAccess.abstracts;

import hrms.hrms.core.login.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByLoginAndPassword(String login, String password);

    Optional<UserModel> findFirstByLogin(String login);

    UserModel findByLoginOrEmail(String login, String email);

}