package vn.hdoan.laptopshop.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hdoan.laptopshop.domain.Product;
import vn.hdoan.laptopshop.domain.User;
import vn.hdoan.laptopshop.service.UploadService;
import vn.hdoan.laptopshop.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    // DI: dependency injection
    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
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
    public String getUserPage(Model model,
                              @RequestParam("page") Optional<String> pageOptional) {
        // client: page = 1, limit = 10
        // database: offset + limit
        int page = 1;
        try {
            if(pageOptional.isPresent()){
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            }else{
                // page = 1
            }
        } catch (Exception e){
            // page = 1
//             TODO: handle Exception
        }

        Pageable pageable = PageRequest.of(page - 1, 2);
        Page<User> user = this.userService.getAllUsers(pageable);
        List<User> listUser = user.getContent();
        model.addAttribute("users1", listUser);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", user.getTotalPages());
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @GetMapping(value = "/admin/user/create") //GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping(value = "/admin/user/create")
    public String createUserPage(Model model, @ModelAttribute("newUser") @Valid User user,
                                 //validation data
                                 BindingResult newUserBindingResult,
                                 @RequestParam("usersFile") MultipartFile file) {

//        List<FieldError> errors = newUserBindingResult.getFieldErrors();
//        for (FieldError error : errors ) {
//            System.out.println (">>>> " + error.getField() + " - " + error.getDefaultMessage());
//        }

        // validate
        if (newUserBindingResult.hasErrors()){
            return "admin/user/create";
        }

        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(user.getPassword());

        user.setAvatar(avatar);
        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName(user.getRole().getName()));
        this.userService.handleSaveUser(user);
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User user ) {
        User currentUser = this.userService.getUserById(user.getId());
        if(currentUser != null){
            currentUser.setAddress(user.getAddress());
            currentUser.setFullName(user.getFullName());
            currentUser.setPhone(user.getPhone());

            // bug here
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id){
        model.addAttribute("id", id);
//        User user = new User();
//        user.setId(id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User user){
        this.userService.deleteAuser(user.getId());
        return "redirect:/admin/user";
    }
}


