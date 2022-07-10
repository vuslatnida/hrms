package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.dtos.response.IdentificationNoDto;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface JobSeekersService {
    DataResult<List<JobSeekers>> getAllJobSeekers();
    Result addJobSeekers(JobSeekersDto jobSeekersDto);
    Result sendEmail(String email);
    Result deleteJobSeeker(IdentificationNoDto identificationNoDto);
    DataResult<List<JobSeekers>> getByIdentificationNoContains(String identificationNo);
    Result exportToExcelJobSeekers(HttpServletResponse response);
    Result exportToPdfJobSeekers(HttpServletResponse response);
}
