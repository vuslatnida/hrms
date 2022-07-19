package hrms.hrms.core.entities.dtos.request;

import lombok.Data;

@Data
public class UserAddDto {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String password;
}
