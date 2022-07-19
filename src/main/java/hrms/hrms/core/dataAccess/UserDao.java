package hrms.hrms.core.dataAccess;

import hrms.hrms.core.entities.User;
import hrms.hrms.core.entities.dtos.response.UserListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByUserId(Integer userId);

    @Query("Select new hrms.hrms.core.entities.dtos.response.UserListDto"
            + "(u.userId, u.email, u.firstName, u.lastName, u.phoneNumber)"
            + " From User u")
    List<UserListDto> findByUserListDto();

    @Query("Select new hrms.hrms.core.entities.dtos.response.UserListDto"
            + "(u.userId, u.email, u.firstName, u.lastName, u.phoneNumber)"
            + " From User u"
            + " WHERE u.email = ?1")
    UserListDto findByEmailToUserListDto(String email);
}