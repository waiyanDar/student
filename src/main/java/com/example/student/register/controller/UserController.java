package com.example.student.register.controller;

import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.entity.User;

import com.example.student.register.security.annotation.UserCreate;
import com.example.student.register.security.annotation.UserDelete;
import com.example.student.register.security.annotation.UserRead;
import com.example.student.register.security.annotation.UserUpdate;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ModelAttribute("loginDate")
    public String loginDate() {
        return LocalDate.now().toString();
    }

    @ModelAttribute("userId")
    public String userId(){
        return oUserId;
    }
    @GetMapping("/registerUser")
    @UserCreate
    public String registerForm(Model model) {
        modelForUser(model, false, "/registerUser", "");
        model.addAttribute("user", new UserRegisterDto());
        return "user-form";
    }

    @PostMapping("/registerUser")
    @UserCreate
    public String registerUser(@Valid UserRegisterDto userDto, BindingResult result, RedirectAttributes attributes,
                               Model model) {

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())){
            result.rejectValue("confirmPassword", "error.userDto", "Password and confirm password must match");
        }

        if (result.hasErrors()) {
            checkValidation(result,model);
            modelForUser(model, false, "/registerUser", "");
            model.addAttribute("user", userDto);
            return "user-form";
        }
        User user = User.formForRegisteration(userDto);
        try {
            userService.registerUser(user, userDto.getRoles());
            oUserId = user.getUserId();
            attributes.addFlashAttribute("success", true);
            return "redirect:/findAllUser";
        } catch (DataIntegrityViolationException e) {
            result.addError(new FieldError("userDto", "email", "Email is taken"));
            checkValidation(result,model);
            modelForUser(model,  false, "/registerUser", "");
            model.addAttribute("user", userDto);
            return "user-form";
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
        modelForUser(model, true,"/userUpdate", oUserId);
        model.addAttribute("user", UserUpdateDto.form(oUser));
        return "user-form";
    }

    @PostMapping("/userUpdate")
    @UserUpdate
    public String updateUser(@Valid UserUpdateDto userDto,BindingResult result, RedirectAttributes attributes,  Model model) {

        if (result.hasErrors()) {
            checkValidation(result, model);
            modelForUser(model, true, "/userUpdate", oUserId);
            model.addAttribute("user", userDto);
            return "user-form";
        }
        User user = User.formForUpdate(userDto);
        user.setId(oId);
        user.setUserId(oUserId);
        try {
            userService.updateUser(user, userDto.getRoles());
            attributes.addFlashAttribute("updateSuccess", true);
            return "redirect:/findAllUser";
        } catch (DataIntegrityViolationException e) {
            result.addError(new FieldError("userDto", "email", "Email is taken"));
            checkValidation(result, model);
            modelForUser(model, true, "/userUpdate", oUserId);
            model.addAttribute("user", userDto);
            return "user-form";
        }

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

        oUserId =  userService.deleteUser(id);
        attributes.addFlashAttribute("deleteUser", true);
        return "redirect:/findAllUser";
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
    	User user = UserService.loginUser;
    	model.addAttribute("user",user );
    	modelForUser(model, false, "/changePsw", "");
    	return "profile";
    }
    
    @GetMapping("/changePsw")
    public String uiChange(Model model) {
    	model.addAttribute("user", UserService.loginUser);
    	modelForUser(model, true, "/changePsw", "");
    	return "profile";
    }
    
    @PostMapping("/changePsw")
    public String updatePassword(@RequestParam("password") String password, 
    							 @RequestParam("confirmPassword") String confirmPassword, BindingResult result, Model model) {
    	
    	if(result.hasErrors()) {
    		model.addAttribute("user", UserService.loginUser);
        	modelForUser(model, true, "/changePsw", "");
    	}
    	userService.changePassword(password, confirmPassword, result, model);
    	model.addAttribute("user", UserService.loginUser);
    	modelForUser(model, false, "/changePsw", "");
    	
    	return "profile";
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

    private void modelForUser(Model model, boolean oldUser, String  link, String oUserId) {
        model.addAttribute("oldUser", oldUser);
//        model.addAttribute("user", userDto);
        model.addAttribute("role", roleService.findAllRole());
        model.addAttribute("actionUrlForU", link);
        model.addAttribute("userId", oUserId);
    }

    private void checkValidation(BindingResult result, Model model){
        if (result.hasFieldErrors("username")){
            model.addAttribute("invalidName", result.getFieldError("username").getDefaultMessage());
        }
        if (result.hasFieldErrors("email")){
            model.addAttribute("invalidEmail", result.getFieldError("email").getDefaultMessage());
        }
        if (result.hasFieldErrors("password")){
            model.addAttribute("invalidPassword", result.getFieldError("password").getDefaultMessage());
        }
        if (result.hasFieldErrors("confirmPassword")){
            model.addAttribute("invalidCPsw", result.getFieldError("confirmPassword").getDefaultMessage());
        }
        if (result.hasFieldErrors("roles")){
            model.addAttribute("invalidRole", result.getFieldError("roles").getDefaultMessage());
        }
    }
}
