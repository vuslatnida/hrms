package hrms.hrms.core.login.business.concretes;

import hrms.hrms.core.login.dataAccess.abstracts.UserRepository;
import hrms.hrms.core.login.entities.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersManager {

    @Autowired
    private UserRepository userRepository;

    public UserModel registerUser(String login, String password, String email){
        if(login == null && password == null){
            return null;
        }

        else{

            if(userRepository.findFirstByLogin(login).isPresent()){
                return null;
            }

            UserModel userModel = new UserModel();
            userModel.setLogin(login);
            userModel.setPassword(password);
            userModel.setEmail(email);
            return userRepository.save(userModel);
        }
    }

    public UserModel authenticate(String login, String password){
        return  userRepository.findByLoginAndPassword(login, password).orElse(null);
    }
}
