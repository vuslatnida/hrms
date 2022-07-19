package hrms.hrms.business.concretes;

import hrms.hrms.business.abstracts.UserService;
import hrms.hrms.core.dataAccess.UserDao;
import hrms.hrms.core.entities.User;
import hrms.hrms.core.entities.dtos.request.UserAddDto;
import hrms.hrms.core.entities.dtos.request.UserChangePasswordDto;
import hrms.hrms.core.entities.dtos.request.UserLoginRequestDto;
import hrms.hrms.core.entities.dtos.request.UserUpdateDto;
import hrms.hrms.core.entities.dtos.response.UserListDto;
import hrms.hrms.core.entities.dtos.response.UserLoginResponseDto;
import hrms.hrms.core.security.jwt.JwtUtils;
import hrms.hrms.core.utilities.convertes.EntityDtoConverter;
import hrms.hrms.core.utilities.results.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserManager implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    private EntityDtoConverter<UserAddDto, User> userAddRequestDtoToUserConverter = new EntityDtoConverter(User.class);

    private EntityDtoConverter<UserUpdateDto, User> userUpdateRequestDtoToUserConverter = new EntityDtoConverter(User.class);

    @Override
    public DataResult<List<UserListDto>> getAllUser() {
        try {
            List<UserListDto> userListResponseDtos = userDao.findByUserListDto();
            return new SuccessDataResult<List<UserListDto>>(userListResponseDtos);
        }

        catch (Exception ex) {
            return new ErrorDataResult<List<UserListDto>>("Bilinmeyen Bir Hata Oluştu");
        }
    }

    @Override
    public Result singup(UserAddDto userAddRequestDto) {
        try {
            if (userDao.existsByEmail(userAddRequestDto.getEmail())) {
                return new ErrorResult( "Bu Email Kullanılıyor");
            }

            else {
                User newUser = userAddRequestDtoToUserConverter.convert(userAddRequestDto);
                newUser.setPassword(passwordEncoder.encode("123456789"));
                newUser.setIsPasswordChanged(true);
                userDao.save(newUser);
                return new SuccessResult();
            }
        }

        catch (Exception ex) {
            return new ErrorResult( "Bilinmeyen Bir Hata Oluştu");
        }
    }

    @Override
    public DataResult<UserLoginResponseDto> login(UserLoginRequestDto userLoginRequestDto) {

        try{
            User user = userDao.findByEmail(userLoginRequestDto.getEmail());
            if(Objects.isNull(user)){
                return new ErrorDataResult<UserLoginResponseDto>("Kullanıcı Bulunamadı");
            }

            try {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword()));
            }

            catch (Exception ex){
                return new ErrorDataResult<UserLoginResponseDto>("Giriş yapmadan önce şifrenizi yenileyiniz.");
            }

            String jwt = jwtUtils.generateJwtToken(user);
            UserListDto userListResponseDto = userDao.findByEmailToUserListDto(userLoginRequestDto.getEmail());

            UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto(
                    userListResponseDto.getUserId(),
                    userListResponseDto.getEmail(),
                    userListResponseDto.getFirstName(),
                    userListResponseDto.getLastName(),
                    userListResponseDto.getPhoneNumber(),
                    jwt
            );

            userLoginResponseDto.setJwt(jwt);
            return new SuccessDataResult<UserLoginResponseDto>(userLoginResponseDto);
        }

        catch (Exception ex){
            return new ErrorDataResult<UserLoginResponseDto>("Bilinmeyen Bir Hata Oluştu : " + ex);
        }
    }



    @Override
    public Result changePassword(UserChangePasswordDto userChangePasswordRequestDto) {
        try{
            User user = userDao.findByEmail(userChangePasswordRequestDto.getEmail());

            if(Objects.isNull(user)){
                return new ErrorResult("Kullanıcı Bulunamadı");
            }

            else if(!user.getIsPasswordChanged()){
                return new ErrorResult("Şifre Değiştirilemez");
            }
            user.setPassword(passwordEncoder.encode(userChangePasswordRequestDto.getPassword()));
            user.setIsPasswordChanged(false); // şifre değiştirildi false
            userDao.save(user);
            return new SuccessResult();
        }

        catch (Exception ex){
            return new ErrorResult("Bilinmeyen Bir Hata Oluştu");
        }
    }

    @Override
    public Result deleteUser(Integer userId) {
        try {
            User user = userDao.findByUserId(userId);
            if(Objects.isNull(user)){
                return new ErrorResult("Kullanıcı Bulunamadı");
            }
            userDao.deleteById(userId);
            return new SuccessResult("Kullanıcı Silindi");
        }

        catch (Exception ex){
            return new ErrorResult("Bilinmeyen Bir Hata Oluştu");
        }
    }

    @Override
    public Result updateUser(Integer userId, UserUpdateDto userUpdateRequestDto) {
        try {
            User oldUser = userDao.findByUserId(userId);

            if(Objects.isNull(oldUser)){
                return new ErrorResult("Kullanıcı Bulunamadı");
            }
            User findUser = userDao.findByEmail(userUpdateRequestDto.getEmail());

            if(!Objects.isNull(findUser)){
                if(findUser.getUserId() != userId){
                    return new ErrorResult("Bu Email Kullanılıyor");
                }
            }
            User updateUser = userUpdateRequestDtoToUserConverter.convert(userUpdateRequestDto);
            updateUser.setUserId(userId);
            updateUser.setPassword(oldUser.getPassword());
            updateUser.setIsPasswordChanged(oldUser.getIsPasswordChanged());
            userDao.save(updateUser);
            return new SuccessResult();
        }

        catch (Exception ex){
            return new ErrorResult("Bilinmeyen Bir Hata Oluştu");
        }
    }
}
