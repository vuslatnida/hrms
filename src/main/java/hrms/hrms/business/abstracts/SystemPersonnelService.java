package hrms.hrms.business.abstracts;

import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;
import hrms.hrms.entities.concretes.SystemPersonnel;
import hrms.hrms.entities.concretes.dtos.SystemPersonnelDto;
import hrms.hrms.entities.concretes.dtos.request.PositionDto;

import java.util.List;

public interface SystemPersonnelService {
    DataResult<List<SystemPersonnel>> getAllSystemPersonnel();
    Result addSystemPersonnel(SystemPersonnelDto systemPersonnelDto);
    Result deleteSystemPersonnel(int id, PositionDto positionDto);
    DataResult<List<SystemPersonnel>> getByJobpositionContains(String jobPosition);
}
