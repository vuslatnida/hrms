package hrms.hrms.entities.concretes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDto {

    @NotNull
    @NotBlank
    private String companyName;

    @NotNull
    @NotBlank
    private String website;

    @NotNull
    @NotBlank
    @Email
    @Size(min = 3, max = 50)
    private String websiteMail;

    @NotNull
    @NotBlank
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^\\d{11}$")
    private String phoneNo;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String jobposition;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;
}
