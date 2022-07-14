package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.Employer;
import hrms.hrms.entities.concretes.dtos.EmployerDto;
import hrms.hrms.entities.concretes.dtos.request.PhoneNoDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface EmployerService {
    DataResult<List<EmployerDto>> getAllEmployers();
    DataResult <List<EmployerDto>> getAllPage(int pageNo, int pageSize);
    Result addEmployer(EmployerDto employerDto);
    Result deleteEmployer(PhoneNoDto phoneNoDto);
    DataResult<List<Employer>> getByCompanyNameContains(String companyName);
    DataResult<List<Employer>> getByWebsiteMailContains(String webMail);
    Result updateEmployerPhoneNo(int id, String phoneNo);
    Result updateEmployerPassword(int id, String password);
    Result exportToExcelEmployer(HttpServletResponse response);
    Result exportToPdfEmployer(HttpServletResponse response);
}
