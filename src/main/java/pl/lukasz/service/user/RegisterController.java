package pl.lukasz.service.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.GET;

@Controller
public class RegisterController {

    @GET
    @RequestMapping(value = "/register")
    public String registerForm(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "register";
    }

}
