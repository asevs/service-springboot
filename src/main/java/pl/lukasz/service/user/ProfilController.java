package pl.lukasz.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.lukasz.service.utilities.UserUtilities;
import pl.lukasz.service.validators.ChangePasswordValidator;
import pl.lukasz.service.validators.EditUserProfileValidator;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.Locale;

@Controller
public class ProfilController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @GET
    @RequestMapping(value = "/profile")
    public String showUserProfile(Model model) {
        String username = UserUtilities.getLoggedUser();
        User user = userService.findUserByEmail(username);

        int nrRole = user.getRoles().iterator().next().getId();

        user.setNrRole(nrRole);
        model.addAttribute("user", user);

        return "profile";
    }

    @GET
    @RequestMapping(value = "/editpassword")
    public String changeUserPassword(Model model) {
        String username = UserUtilities.getLoggedUser();
        User user = userService.findUserByEmail(username);
        model.addAttribute("user", user);
        return "editpassword";

    }

    @POST
    @RequestMapping(value = "/updatepass")
    public String changeUSerPassword(User user, BindingResult result, Model model, Locale locale) {
        String returnPage = null;

        new ChangePasswordValidator().validate(user, result);

        new ChangePasswordValidator().checkPasswords(user.getNewPassword(), result);

        if (result.hasErrors()) {
            returnPage = "editpassword";
        } else {
            userService.updateUserPassword(user.getNewPassword(), user.getEmail());
            returnPage = "editpassword";
            model.addAttribute("message", messageSource.getMessage("passwordChange.success", null, locale));
        }

        return returnPage;
    }

    @GET
    @RequestMapping(value = "/editprofile")
    public String changeUserData(Model model) {
        String username = UserUtilities.getLoggedUser();
        User user = userService.findUserByEmail(username);
        model.addAttribute("user", user);
        return "editprofile";
    }

    @POST
    @RequestMapping(value = "/updateprofile")
    public String changeUserDataAction(User user, BindingResult result, Model model, Locale locale) {
        String returnPage = null;
        new EditUserProfileValidator().validate(user, result);
        if (result.hasErrors()) {
            returnPage = "editprofile";
        } else {
            userService.updateUserProfile(user.getName(), user.getLastName(), user.getEmail(), user.getId());
            model.addAttribute("message", messageSource.getMessage("editProfile.success", null, locale));
            returnPage = "aftereditprofile";
        }
        return returnPage;
    }


}
