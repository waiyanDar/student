package com.example.student.register.controller;

import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.entity.User;

import com.example.student.register.security.annotation.UserCreate;
import com.example.student.register.security.annotation.UserDelete;
import com.example.student.register.security.annotation.UserRead;
import com.example.student.register.security.annotation.UserUpdate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.student.register.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("loginDate")
    public String loginDate() {
        return LocalDate.now().toString();
    }

    @ModelAttribute("userId")
    public String userId(){
        return userService.getUserId();
    }

    @GetMapping("/registerUser")
    @UserCreate
    public String registerForm(Model model) {

        return userService.getRegisterForm(model);

    }

    @PostMapping("/registerUser")
    @UserCreate
    public String registerUser(@Valid UserRegisterDto userDto, BindingResult result,
                               RedirectAttributes attributes,Model model) {

           return userService.registerUser(userDto, result, attributes, model);
    }

    @GetMapping("/userUpdate")
    @UserUpdate
    public String uiChange(@RequestParam("id") int id, Model model) {

       return userService.getUpdateForm(id, model);

    }

    @PostMapping("/userUpdate")
    @UserUpdate
    public String updateUser(@Valid UserUpdateDto userDto,BindingResult result,
                             RedirectAttributes attributes,  Model model) {

           return userService.updateUser(userDto, result, attributes, model);

    }

    @GetMapping("/findAllUser")
    @UserRead
    public String findAllUser(Model model) {

        model.addAttribute("userList", userService.findAllUser());

        return "user-list";
    }

    @GetMapping("/deleteUser")
    @UserDelete
    public String deleteUser(@RequestParam("id") int id, RedirectAttributes attributes) {

       return userService.deleteUser(id, attributes);
    }
    

    @GetMapping("/searchUser")
    public String searchUser(@RequestParam("userId") Optional<String> userId,
                             @RequestParam("username") Optional<String> username, Model model) {
        List<User> user = userService.searchUser(userId, username);
        model.addAttribute("userList", user);
        model.addAttribute("searchUsername", username.orElse(""));
        return "user-list";
    }
    
    @GetMapping("/profile")
    public String goProfile(Model model) {

        return userService.getDataForProfile(model);

    }
    
    @GetMapping("/changePsw")
    public String uiChange(Model model) {

        return userService.getPswForm(model);

    }
    
    @PostMapping("/changePsw")
    public String updatePassword(@RequestParam("newPassword") String password,
    							 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes attributes, Model model) {

    	return userService.changePassword(password, confirmPassword, model, attributes);

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(RedirectAttributes attributes) {

        attributes.addFlashAttribute("loginError", true);

        return "redirect:/login";
    }

}
