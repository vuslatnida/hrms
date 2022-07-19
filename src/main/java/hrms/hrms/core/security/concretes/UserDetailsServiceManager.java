package hrms.hrms.core.security.concretes;

import hrms.hrms.core.dataAccess.UserDao;
import hrms.hrms.core.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceManager implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userDao.findByEmail(email);
        return new UserDetailsManager(user);
    }
}