package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

@Controller

public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails currentUser ) {
        User user = userService.findByUsername(currentUser.getUsername());
        model.addAttribute("currentUser", user);
        model.addAttribute("users", userService.getUsers());
        return "users/index";
    }

    @GetMapping("admin/open_user")
    public String getUser(@RequestParam(value = "id", required = false) Long id, ModelMap model) {
        model.addAttribute("user", userService.getUserbyId(id));
        return "users/get_user";
    }

    @GetMapping("admin/list_users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users/list_users";
    }

    @GetMapping("admin/add_user")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listRoles", userService.getAllRols());
        return "users/add_user";
    }

    @PostMapping("admin/add_user")
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin/list_users";
    }

    @GetMapping("admin/edit_user")
    public String edit(@RequestParam(value = "id", required = false) Long id, Model model) {
        model.addAttribute("listRoles", userService.getAllRols());
        model.addAttribute("user", userService.getUserbyId(id));
        return "users/edit_user";
    }

    @PatchMapping("admin/edit_user")
    public String edit(@ModelAttribute("user") User user) {
        System.out.println("Controller  "+user.getRoles());
        userService.save(user);
        return "redirect:/admin/list_users";
    }

    @GetMapping("admin/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.delete(id);
        return "redirect:/admin/list_users";
    }

    @GetMapping("/hello_user")
    public String helloPage(Model model, @AuthenticationPrincipal UserDetails curUser) {
        User user = userService.findByUsername(curUser.getUsername());
        model.addAttribute("curUser", user);
        model.addAttribute("users", userService.getUsers());
        return "users/hello_user";
    }
    @RequestMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "/login";
    }
}
