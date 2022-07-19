package hrms.hrms.business.abstracts;

import hrms.hrms.core.entities.dtos.request.UserAddDto;
import hrms.hrms.core.entities.dtos.request.UserChangePasswordDto;
import hrms.hrms.core.entities.dtos.request.UserLoginRequestDto;
import hrms.hrms.core.entities.dtos.request.UserUpdateDto;
import hrms.hrms.core.entities.dtos.response.UserListDto;
import hrms.hrms.core.entities.dtos.response.UserLoginResponseDto;
import hrms.hrms.core.utilities.results.DataResult;
import hrms.hrms.core.utilities.results.Result;

import java.util.List;

public interface UserService {

    DataResult<List<UserListDto>> getAllUser();

    Result singup(UserAddDto userAddRequestDto);

    DataResult<UserLoginResponseDto> login(UserLoginRequestDto userLoginRequestDto);

    Result changePassword(UserChangePasswordDto userChangePasswordRequestDto);

    Result deleteUser(Integer userId);

    Result updateUser(Integer userId, UserUpdateDto userUpdateRequestDto);
}
