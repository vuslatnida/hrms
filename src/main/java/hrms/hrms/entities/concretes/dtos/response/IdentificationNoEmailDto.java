package hrms.hrms.entities.concretes.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class IdentificationNoEmailDto {
    private String identificationNo;
    private String email;
}
