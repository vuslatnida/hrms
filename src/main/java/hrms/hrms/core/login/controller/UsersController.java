package hrms.hrms.core.login.controller;

import hrms.hrms.core.login.business.concretes.UsersManager;
import hrms.hrms.core.login.entities.UserModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    private final UsersManager usersManager;


    public UsersController(UsersManager usersManager) {
        this.usersManager = usersManager;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new UserModel());
        return "register_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("loginRequest", new UserModel());
        return "login_page";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserModel userModel){
        System.out.println("register request : " + userModel);
        UserModel registeredUser = usersManager.registerUser(userModel.getLogin(), userModel.getPassword(), userModel.getEmail());
        return registeredUser == null ? "error_page" : "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserModel userModel, Model model){
        System.out.println("login request : " + userModel);
        UserModel authenticate = usersManager.authenticate(userModel.getLogin(), userModel.getPassword());

        if(authenticate != null){
            model.addAttribute("userLogin", authenticate.getLogin());
            return "redirect:/swagger-ui/";
        }

        else {
            return "error_page";
        }
    }
}
