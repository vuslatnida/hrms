package hrms.hrms.core.entities.dtos.request;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String email;

    private String password;
}
