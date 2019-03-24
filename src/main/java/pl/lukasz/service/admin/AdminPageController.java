package pl.lukasz.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.lukasz.service.user.User;
import pl.lukasz.service.user.UserService;

import javax.ws.rs.GET;
import java.util.List;

@Controller
public class AdminPageController {

    @Autowired
    private UserService userService;

    @GET
    @RequestMapping(value = "/admin")
    @Secured(value = {"ROLE_ADMIN"})
    public String openAdminMainPage() {
        return "admin";
    }

    @GET
    @RequestMapping(value = "/users")
    @Secured(value = {"ROLE_ADMIN"})
    public String openAdminAllUsersPage(Model model) {
        List<User> userList = getAllUsers();
        model.addAttribute("userList", userList);
        return "users";
    }

    private List<User> getAllUsers() {
        List<User> userList = userService.findAll();
        for (User users : userList) {
            int numberRole = users.getRoles().iterator().next().getId();
            if (numberRole == 1) {
                users.setNrRole(numberRole);
            } else if (numberRole == 2) {
                users.setNrRole(numberRole);
            }

        }
        return userList;
    }
}
