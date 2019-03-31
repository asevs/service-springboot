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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.lukasz.service.user.User;
import pl.lukasz.service.utilities.UserUtilities;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Page<User> pages = getAllUsersPageable(page - 1,false,null);
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

    private Page<User> getAllUsersPageable(int page, boolean search, String param) {
        Page<User> pages;
        if (!search) {
            pages = adminService.findAll(PageRequest.of(page, ELEMENTS));
        } else {
            pages = adminService.findAllSearch(param, PageRequest.of(page, ELEMENTS));
        }
        for (User users : pages) {
            int numerRole = users.getRoles().iterator().next().getId();
            users.setNrRole(numerRole);
        }
        return pages;
    }

    @GET
    @RequestMapping(value = "/users/importusers")
    @Secured(value = "ROLE_ADMIN")
    public String showUsersUploadPageFromXML(Model model){
        return "importusers";
    }

    @GET
    @RequestMapping(value = "/users/search/{searchWord}/{page}")
    @Secured(value = "ROLE_ADMIN")
    public String openSearchUsersPage(@PathVariable("searchWord") String searchWord,
                                      @PathVariable("page") int page, Model model) {
        Page<User> pages = getAllUsersPageable(page - 1, true, searchWord);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<User> userList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("userList", userList);
        model.addAttribute("recordStartCounter", currentPage * ELEMENTS);
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("userList", userList);
        return "usersearch";
    }



    @POST
    @RequestMapping(value = "/users/upload")
    @Secured(value = "ROLE_ADMIN")
    public String importUsersFromXML(@RequestParam("filename")MultipartFile multipartFile) {
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        File file;
        try {
            file = new File(uploadDir);
            if (!file.exists()) {
                file.mkdir();
            }
            Path fileAndPath = Paths.get(uploadDir, multipartFile.getOriginalFilename());
            Files.write(fileAndPath, multipartFile.getBytes());
            file = new File(fileAndPath.toString());
            List<User> userList = UserUtilities.userDataLoader(file);

            adminService.insertInBatch(userList);
            adminService.saveAll(userList);
            file.delete();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/users/1";
    }



}
