package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.JobSeekers;
import hrms.hrms.entities.concretes.dtos.response.IdentificationNoDto;
import hrms.hrms.entities.concretes.dtos.response.IdentificationNoEmailDto;
import hrms.hrms.entities.concretes.dtos.JobSeekersDto;

import java.util.List;

public interface JobSeekersService {
    DataResult<List<JobSeekers>> getJobSeekers();
    Result addJobSeekers(JobSeekersDto jobSeekersDto);
    Result getByIdentificationNoAndEmail(IdentificationNoEmailDto identificationNoEmailDto);
    Result sendEmail(String email);
    Result deleteJobSeeker(IdentificationNoDto identificationNoDto);

}
