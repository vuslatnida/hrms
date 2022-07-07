package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.dtos.EmployerDto;
import hrms.hrms.entities.concretes.dtos.response.PhoneNoDto;
import hrms.hrms.entities.concretes.dtos.response.WebMailDto;


import java.util.List;

public interface EmployerService {
    DataResult<List<Employer>> getAllEmployers();

    Result addEmployer(EmployerDto employerDto);
    Result deleteEmployer(PhoneNoDto phoneNoDto);
    Result getByWebsiteMail(WebMailDto webMailDto);
    Result sendWebEmail(EmployerDto employerDto);
    Result hrmsConfirm(EmployerDto employerDto);
}
