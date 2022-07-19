package hrms.hrms.core.entities.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListDto {
    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
