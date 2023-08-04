package com.example.student.register.controller;

import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.explorter.UserExplorer;
import com.example.student.register.security.annotation.Admin;
import com.example.student.register.security.annotation.SuperAdmin;
import com.example.student.register.security.annotation.UserCreate;
import com.example.student.register.security.annotation.UserDelete;
import com.example.student.register.security.annotation.UserRead;
import com.example.student.register.security.annotation.UserUpdate;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.student.register.service.QuartzService;
import com.example.student.register.service.UserService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    private final UserExplorer userExplorer;
    
    private final QuartzService quartzService;

    public UserController(@Lazy UserService userService, @Lazy UserExplorer userExplorer, @Lazy QuartzService quartzService) {
        this.userService = userService;
        this.userExplorer = userExplorer;
        this.quartzService = quartzService;
    }

    @ModelAttribute("loginDate")
    public String loginDate() {
        return LocalDate.now().toString();
    }

    @GetMapping("/registerUser")
    @UserCreate
    public String registerForm(Model model) {

        return userService.getRegisterForm(model);

    }

    @PostMapping("/registerUser")
    @UserCreate
    public String registerUser(@Valid UserRegisterDto userDto, BindingResult result, RedirectAttributes attributes,
                               Model model) {

        return userService.registerUser(userDto, result, attributes, model);
    }

    @GetMapping("/userUpdate")
    @UserUpdate
    public String userUpdateForm(@RequestParam("userId") String userId, Model model) {

        userUpdateId = userId;
        return userService.getUpdateForm(userId, "/userUpdate", model);

    }

    String userUpdateId;

    @PostMapping("/userUpdate")
    @UserUpdate
    public String updateUser(@Valid UserUpdateDto userDto, BindingResult result, RedirectAttributes attributes,
                             Model model) {

        userDto.setUserId(userUpdateId);
        return userService.updateUser(userDto, "/userUpdate", result, attributes, model);

    }

    @GetMapping("/deleteUser")
    @UserDelete
    public String deleteUser(@RequestParam("userId") String userId, RedirectAttributes attributes) {

        return userService.deleteUser(userId, attributes);
    }

    @GetMapping("/changePsw")
    public String uiChange(Model model) {

        return userService.getPswForm(model);

    }

    @PostMapping("/changePsw")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String password, @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes attributes, Model model) {

        return userService.changePassword(oldPassword, password, confirmPassword, model, attributes);

    }

    @GetMapping("/exportUserToExcel")
    @Admin
    public String exportStudentToExcel(RedirectAttributes attributes) {
        userExplorer.exportUserToExcel();
        attributes.addFlashAttribute("exportExcel", true);
        return "redirect:/findAllUser";
    }

    @GetMapping("/findAllUser")
    @UserRead
    public String paginationUser() {

        return "user-list";
    }
        
    @PostMapping("/editReport")
    @SuperAdmin
    public String quartzControl(@RequestParam("date") Optional<String> date, @RequestParam("customDays") Optional<String> days,
    							@RequestParam("time") Optional<String> time, @RequestParam("autoReport") Optional<Boolean> autoReport) {

            quartzService.executeQuartz(date, days, time, autoReport);

    	return "redirect:/findAllUser";
    }

    @ModelAttribute("quartzDate")
    public String quartzDate(){
        return quartzService.getStrDate();
    }

    @ModelAttribute("quartzDay")
    public String quartzDay(){
        return quartzService.getStrDays();
    }

    @ModelAttribute("autoReport")
    public boolean isAutoReport(){
        return quartzService.isBoolAutoReport();
    }

    @ModelAttribute("quartzTime")
    public String quartzTime(){
        return quartzService.getStrTime();
    }

}
