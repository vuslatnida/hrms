package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.dtos.EmployerDto;
import hrms.hrms.entities.concretes.dtos.response.PhoneNoDto;


import java.util.List;

public interface EmployerService {
    DataResult<List<Employer>> getAllEmployers();
    Result addEmployer(EmployerDto employerDto);
    Result deleteEmployer(PhoneNoDto phoneNoDto);
    DataResult<List<Employer>> getByCompanyNameContains(String companyName);
    DataResult<List<Employer>> getByWebsiteMailContains(String webMail);
    Result sendWebEmail(EmployerDto employerDto);
    Result hrmsConfirm(EmployerDto employerDto);
}
