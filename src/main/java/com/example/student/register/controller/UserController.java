package com.example.student.register.controller;

import com.example.student.register.dto.UserDto;
import com.example.student.register.entity.User;

import com.example.student.register.security.annotation.UserCreate;
import com.example.student.register.security.annotation.UserDelete;
import com.example.student.register.security.annotation.UserRead;
import com.example.student.register.security.annotation.UserUpdate;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.student.register.service.UserService;
import com.example.student.register.service.RoleService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

@Controller
public class UserController {

    private UserService userService;

    private RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ModelAttribute("loginDate")
    public String loginDate() {
        return LocalDate.now().toString();
    }

    @GetMapping("/registerUser")
    @UserCreate
    public String registerForm(Model model) {
		model.addAttribute("oldUser", false);
        model.addAttribute("user", new UserDto());
        model.addAttribute("role", roleService.findAllRole());
        model.addAttribute("actionUrlForU", "/registerUser");
        return "userRegisterForm";
    }

    @PostMapping("/registerUser")
    @UserCreate
    public String registerUser(@Valid UserDto userDto, BindingResult result, RedirectAttributes attributes,
                               Model model) {

        if (result.hasErrors()) {
            return "redirect:/registerUser";
        }
        User user = User.form(userDto);
        try {
            userService.registerUser(user, userDto.getRoles());
            attributes.addFlashAttribute("usrId", user.getUserId());
            attributes.addFlashAttribute("success", true);
            return "redirect:/findAllUser";
        } catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("emailDuplicate", true);
            return "redirect:/registerUser";
        }

    }

    String oUserId;
    int oId;

    @GetMapping("/userUpdate")
    @UserUpdate
    public String uiChange(@RequestParam("id") int id, Model model) {
        User oUser = userService.findUserById(id);
        oUserId = oUser.getUserId();
        oId = oUser.getId();
		model.addAttribute("oldUser", true);
        model.addAttribute("user", UserDto.form(oUser));
        model.addAttribute("role", roleService.findAllRole());
        model.addAttribute("actionUrlForU", "/userUpdate");
        return "userRegisterForm";
    }

    @PostMapping("/userUpdate")
    @UserUpdate
    public String updateUser(@Valid UserDto userDto, RedirectAttributes attributes, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "redirect:/userUpdate?id=" + oId;
        }
        User user = User.form(userDto);
        user.setId(oId);
        user.setUserId(oUserId);
        try {
            userService.updateUser(user, userDto.getRoles());
            model.addAttribute("usrId", user.getId());
            attributes.addFlashAttribute("updateSuccess", true);
            return "redirect:/findAllUser";
        } catch (DataIntegrityViolationException e) {
            attributes.addFlashAttribute("emailDuplicate", true);
            return "redirect:/userUpdate?id=" + oId;
        }

    }

    @GetMapping("/findAllUser")
    @UserRead
    public String findAllUser(Model model) {

        model.addAttribute("userList", userService.findAllUser());

        return "userList";
    }

    @GetMapping("/deleteUser")
    @UserDelete
    public String deleteUser(@RequestParam("id") int id, RedirectAttributes attributes) {

        userService.deleteUser(id);
        attributes.addFlashAttribute("deleteUser", true);
        return "redirect:/findAllUser";
    }

    @GetMapping("/searchUser")
    public String searchUser(@RequestParam("userId") Optional<String> userId,
                             @RequestParam("username") Optional<String> username, Model model) {
        List<User> user = userService.searchUser(userId, username);
        model.addAttribute("userList", user);
        model.addAttribute("searchUserId", userId.orElse(""));
        model.addAttribute("searchUsername", username.orElse(""));
        return "userList";
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
