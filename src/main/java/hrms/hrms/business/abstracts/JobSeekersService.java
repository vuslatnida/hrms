package hrms.hrms.business.abstracts;

import hrms.hrms.core.entities.dtos.JobSeekersGetDto;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface JobSeekersService {

    DataResult<List<JobSeekersGetDto>> getAllJobSeekers();

    DataResult <List<JobSeekersGetDto>> getAllPage(int pageNo, int pageSize);

    Result addJobSeekers(JobSeekersDto jobSeekersDto);

    Result deleteJobSeeker(int id);

    Result updatePassword(int id, String password);

    DataResult<List<JobSeekers>> getByIdentificationNoContains(String identificationNo);

    Result exportToExcelJobSeekers(HttpServletResponse response);

    Result exportToPdfJobSeekers(HttpServletResponse response);
}
