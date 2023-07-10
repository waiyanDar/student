package com.example.student.register.service;

import java.util.List;
import java.util.Optional;

import static com.example.student.register.dao.SpecificationUtil.*;

import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.entity.User;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.student.register.dao.UserDao;
import com.example.student.register.dao.RoleDao;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private PasswordEncoder passwordEncoder;

    private String userId;
    private int id;

    public static User loginUser;

    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public String getUserId() {
        return userId;
    }

    //    public void registerUser(User user, List<Role> roles) {
    public String registerUser(UserRegisterDto userRegisterDto, BindingResult result,
                             RedirectAttributes attributes, Model model) {

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.userDto", "Password and confirm password must match");
        }
        if (result.hasErrors()) {
            checkValidation(result,model);
            modelForUser(model,userRegisterDto, false, "/registerUser", "");
            return "user-form";
        }
        User user = User.formForRegistration(userRegisterDto);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRoles(userRegisterDto.getRoles());
        try {
            userDao.save(user);
            attributes.addFlashAttribute("success", true);
            userId = user.getUserId();
            return "redirect:/findAllUser";
        }catch (DataIntegrityViolationException e){
            result.addError(new FieldError("userDto", "email", "Email is taken"));
            checkValidation(result,model);
            modelForUser(model,userRegisterDto,  false, "/registerUser", "");
            return "user-form";
        }

    }

    public User findUserById(int id) {
        this.id = id;
        User user = userDao.findById(id).get();
        userId = user.getUserId();
        return user;
    }


    public User findUserByUserId(String userId) {
        loginUser = userDao.findUserByUserId(userId).get();
        return loginUser;
    }

    public String deleteUser(int id, RedirectAttributes attributes) {
        User user = findUserById(id);
        userId = user.getUserId();
        userDao.delete(user);
        attributes.addFlashAttribute("deleteUser", true);
        return "redirect:/findAllUser";
    }

    public String updateUser(UserUpdateDto userUpdateDto, BindingResult result,
                             RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {
            checkValidation(result, model);
            modelForUser(model, userUpdateDto,true, "/userUpdate", userId);
//            model.addAttribute("user", userUpdateDto);
            return "user-form";
        }

        User oUser = findUserByUserId(userId);
        oUser.setUsername(userUpdateDto.getUsername());
        oUser.setEmail(userUpdateDto.getEmail());
        oUser.deleteRole();
        oUser.setRoles(userUpdateDto.getRoles());

        try {
            userDao.saveAndFlush(oUser);
            attributes.addFlashAttribute("updateSuccess", true);
            return "redirect:/findAllUser";
        } catch (DataIntegrityViolationException e) {
            result.addError(new FieldError("userDto", "email", "Email is taken"));
            checkValidation(result, model);
            modelForUser(model, userUpdateDto,true, "/userUpdate", userId);
//            model.addAttribute("user", userUpdateDto);
            return "user-form";
        }
    }

    public String changePassword(String password, String confirmPassword, Model model, RedirectAttributes attributes) {

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            model.addAttribute("pswBlank", "Please fill in all the fields.");
            modelForUser(model,UserService.loginUser, true, "/changePsw", "");
            return "profile";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("pswNoMatch", "Passwords do not match.");
            modelForUser(model,UserService.loginUser, true, "/changePsw", "");
            return "profile";
        }
        User oUser = findUserByUserId(loginUser.getUserId());
        oUser.setPassword(passwordEncoder.encode(password));

        userDao.saveAndFlush(oUser);
        modelForUser(model, UserService.loginUser,false, "/changePsw", "");
        attributes.addFlashAttribute("successChangePsw", true);
        return "profile";
    }

    public List<User> findAllUser() {
        return userDao.findAll();
    }

    public List<User> searchUser(Optional<String> userId, Optional<String> name) {
        Specification<User> specification = withUserId(userId).and(withName(name));
        return userDao.findAll(specification);
    }


    private <T> void modelForUser(Model model, T userDto, boolean oldUser, String link, String oUserId) {
        model.addAttribute("oldUser", oldUser);
        model.addAttribute("user", userDto);
        model.addAttribute("role", roleDao.findAll());
        model.addAttribute("actionUrlForU", link);
        model.addAttribute("userId", oUserId);
    }

    private void checkValidation(BindingResult result, Model model) {
        if (result.hasFieldErrors("username")) {
            model.addAttribute("invalidName", result.getFieldError("username").getDefaultMessage());
        }
        if (result.hasFieldErrors("email")) {
            model.addAttribute("invalidEmail", result.getFieldError("email").getDefaultMessage());
        }
        if (result.hasFieldErrors("password")) {
            model.addAttribute("invalidPassword", result.getFieldError("password").getDefaultMessage());
        }
        if (result.hasFieldErrors("confirmPassword")) {
            model.addAttribute("invalidCPsw", result.getFieldError("confirmPassword").getDefaultMessage());
        }
        if (result.hasFieldErrors("roles")) {
            model.addAttribute("invalidRole", result.getFieldError("roles").getDefaultMessage());
        }
    }

    public String getRegisterForm(Model model) {
        modelForUser(model, new UserRegisterDto(), false, "/registerUser", "");
//        model.addAttribute("user", new UserRegisterDto());
        return "user-form";
    }

    public String getUpdateForm(int id, Model model) {
        User user = findUserById(id);
        userId = user.getUserId();
        this.id = user.getId();
        modelForUser(model,UserUpdateDto.form(user), true,"/userUpdate", userId);
//        model.addAttribute("user", UserUpdateDto.form(oUser));
        return "user-form";
    }

    public String getDataForProfile(Model model) {
//        User user = UserService.loginUser;
//        model.addAttribute("user",user );
        modelForUser(model, loginUser,false, "/changePsw", "");
        return "profile";
    }

    public String getPswForm(Model model) {
//        model.addAttribute("user", UserService.loginUser);
        modelForUser(model, loginUser,true, "/changePsw", "");
        return "profile";
    }
}
