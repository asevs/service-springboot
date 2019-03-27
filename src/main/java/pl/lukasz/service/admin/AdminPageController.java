package pl.lukasz.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.lukasz.service.user.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class AdminPageController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MessageSource messageSource;

    private static int ELEMENTS = 10;


    @GET
    @RequestMapping(value = "/admin")
    @Secured(value = {"ROLE_ADMIN"})
    public String openAdminMainPage() {
        return "admin";
    }

    @GET
    @RequestMapping(value = "/users/{page}")
    @Secured(value = {"ROLE_ADMIN"})
    public String openAdminAllUsersPage(@PathVariable("page") int page, Model model) {
        Page<User> pages = getAllUsersPageable(page - 1);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<User> userList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("userList", userList);
        return "users";
    }

    @GET
    @RequestMapping(value = "/users/edit/{id}")
    @Secured(value = {"ROLE_ADMIN"})
    public String getUsersToEdit(@PathVariable("id") int id, Model model) {
        User user = new User();
        user = adminService.findUserByID(id);

        Map<Integer, String> roleMap = new HashMap<Integer, String>();
        roleMap = prepareRoleMap();

        Map<Integer, String> activityMap = new HashMap<Integer, String>();
        activityMap = prepareActivityMap();

        int role = user.getRoles().iterator().next().getId();
        user.setNrRole(role);

        model.addAttribute("roleMap", roleMap);
        model.addAttribute("activityMap", activityMap);
        model.addAttribute("user", user);

        return "useredit";
    }

    @POST
    @RequestMapping(value = "updateuser/{id}")
    @Secured(value = {"ROLE_ADMIN"})
    public String updateUser(@PathVariable("id") int id, User user) {
        int nrRole = user.getNrRole();
        int orActive = user.getActive();
        adminService.updateUser(id, nrRole, orActive);
        return "redirect:/users/1";
    }

    private Map<Integer, String> prepareActivityMap() {
        Locale locale = Locale.getDefault();
        Map<Integer, String> activityMap = new HashMap<Integer, String>();
        activityMap.put(1, messageSource.getMessage("login.active", null, locale));
        activityMap.put(2, messageSource.getMessage("login.noactive", null, locale));
        return activityMap;
    }

    private Map<Integer, String> prepareRoleMap() {
        Locale locale = Locale.getDefault();
        Map<Integer, String> roleMap = new HashMap<Integer, String>();
        roleMap.put(1, messageSource.getMessage("word.admin", null, locale));
        roleMap.put(2, messageSource.getMessage("word.user", null, locale));
        return roleMap;
    }

    private Page<User> getAllUsersPageable(int page) {
        int elements = 2;
        Page<User> pages = adminService.findAll(PageRequest.of(page, elements));
        for (User users : pages) {
            int numberRole = users.getRoles().iterator().next().getId();
            users.setNrRole(numberRole);


        }
        return pages;
    }


    @GET
    @RequestMapping(value = "users/search/{searchWord}")
    @Secured(value = "ROLE_ADMIN")
    public String openSearchUsersPage(@PathVariable("searchWord") String searchWord, Model model) {
        List<User> userList = adminService.findAllSearch(searchWord);
        for (User users : userList) {
            int numerRole = users.getRoles().iterator().next().getId();
            users.setNrRole(numerRole);
        }
        model.addAttribute("userList", userList);
        return "usersearch";
    }
}
