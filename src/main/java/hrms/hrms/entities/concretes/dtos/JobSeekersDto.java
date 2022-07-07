package hrms.hrms.entities.concretes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class JobSeekersDto {

    @NotNull
    @NotBlank
    @Size(min = 11, max = 11)
    private String identificationNo;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 4)
    private String birthYear;

    @NotNull
    @NotBlank
    @Email
    @Size(min = 3, max = 50)
    private String email;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String jobposition;

}
