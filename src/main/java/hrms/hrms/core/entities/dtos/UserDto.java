package hrms.hrms.core.entities.dtos;

import lombok.Data;

@Data
public class UserDto {

    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}