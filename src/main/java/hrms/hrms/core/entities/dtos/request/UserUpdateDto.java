package hrms.hrms.core.entities.dtos.request;

import lombok.Data;

@Data

public class UserUpdateDto {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
