package hrms.hrms.business.abstracts;

import hrms.hrms.core.entities.dtos.EmployerGetDto;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.dtos.EmployerDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface EmployerService {
    DataResult<List<EmployerGetDto>> getAllEmployers();
    DataResult <List<EmployerGetDto>> getAllPage(int pageNo, int pageSize);
    Result addEmployer(EmployerDto employerDto);
    Result deleteEmployer(int id);
    DataResult<List<Employer>> getByCompanyNameContains(String companyName);
    DataResult<List<Employer>> getByWebsiteMailContains(String webMail);
    Result updatePhoneNo(int id, String phoneNo);
    Result updatePassword(int id, String password);
    Result exportToExcelEmployer(HttpServletResponse response);
    Result exportToPdfEmployer(HttpServletResponse response);
}
