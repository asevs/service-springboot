package pl.lukasz.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.lukasz.service.utilities.UserUtilities;

import javax.ws.rs.GET;
@Controller
public class ProfilController {
    @Autowired
    private UserService userService;

    @GET
    @RequestMapping(value = "/profile")
    public String showUserProfile(Model model){
        String username = UserUtilities.getLoggedUser();
        User user = userService.findUserByEmail(username);

        int nrRole = user.getRoles().iterator().next().getId();

        user.setNrRole(nrRole);
        model.addAttribute("user", user);

        return  "profile";
    }
}
