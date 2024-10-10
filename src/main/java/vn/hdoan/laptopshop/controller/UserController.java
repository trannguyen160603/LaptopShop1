package vn.hdoan.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.hdoan.laptopshop.domain.User;
import vn.hdoan.laptopshop.service.UserService;

import java.util.List;

@Controller
public class UserController {

    // DI: dependency injection
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model){
        List<User> arrUsers = this.userService.getAllUsersByEmail("abc@gmail.com");
        System.out.println(arrUsers);
        String test = this.userService.handleHello();
        model.addAttribute("eric", test);
        model.addAttribute("hoidanit", "from controller with model");
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        return "admin/user/table-user";
    }

    @RequestMapping(value = "/admin/user/create") //GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createUserPage(Model model, @ModelAttribute("newUser") User user) {
        System.out.println("run here " + user);
        this.userService.handleSaveUser(user);
        return "hello";
    }
}


