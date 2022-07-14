package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.dtos.request.IdentificationNoDto;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface JobSeekersService {
    DataResult<List<JobSeekersDto>> getAllJobSeekers();
    DataResult <List<JobSeekersDto>> getAllPage(int pageNo, int pageSize);
    Result addJobSeekers(JobSeekersDto jobSeekersDto);
    Result deleteJobSeeker(IdentificationNoDto identificationNoDto);
    Result updateJobSeeker(int id, String password);
    DataResult<List<JobSeekers>> getByIdentificationNoContains(String identificationNo);
    Result exportToExcelJobSeekers(HttpServletResponse response);
    Result exportToPdfJobSeekers(HttpServletResponse response);
}
