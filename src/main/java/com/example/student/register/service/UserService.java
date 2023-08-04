package com.example.student.register.service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import com.example.student.register.dao.SpecificationUtil;
import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.entity.Role;
import com.example.student.register.entity.User;
import com.example.student.register.generator.OtpGenerator;
import com.example.student.register.holder.Holder;
import com.example.student.register.security.DecryptPassword;

import lombok.Getter;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.student.register.dao.UserDao;
import com.example.student.register.dao.RoleDao;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@Getter
public class UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final PasswordEncoder passwordEncoder;

    private final OtpGenerator otpGenerator;

    private final Holder holder;

    private final DecryptPassword decryptPassword;

    private final JavaMailSender javaMailSender;

    private String userId;

    public static User loginUser;

    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder, OtpGenerator otpGenerator,
                       JavaMailSender javaMailSender, DecryptPassword decryptPassword, Holder holder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.otpGenerator = otpGenerator;
        this.javaMailSender = javaMailSender;
        this.decryptPassword = decryptPassword;
        this.holder = holder;
    }

    public String getUserId() {
        return userId;
    }

    public String registerUser(UserRegisterDto userRegisterDto, BindingResult result, RedirectAttributes attributes,
                               Model model) {

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.userDto", "Password and confirm password must match");
        }

        if (result.hasErrors()) {

            checkValidation(result, model);
            modelForUser(model, userRegisterDto, false, "/registerUser", "");
            return "user-form";
        }

        User user = null;

        try {
            user = User.formForRegistration(userRegisterDto);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setRoles(userRegisterDto.getRoles());

        try {

            userDao.save(user);
            attributes.addFlashAttribute("success", user.getUserId());
            userId = user.getUserId();

            return "redirect:/findAllUser";

        } catch (DataIntegrityViolationException e) {

            result.addError(new FieldError("userDto", "email", "Email is taken"));
            checkValidation(result, model);
            modelForUser(model, userRegisterDto, false, "/registerUser", "");

            return "user-form";
        }

    }

    public User findUserById(int id) {

        User user = userDao.findById(id).get();
        userId = user.getUserId();

        return user;
    }

    public User findUserByUserId(String userId) {

        loginUser = userDao.findUserByUserId(userId).get();

        return loginUser;
    }

    public String deleteUser(String id, RedirectAttributes attributes) {

        User user = findUserByUserId(id);
        userId = user.getUserId();
        userDao.delete(user);

        attributes.addFlashAttribute("deleteUser", user.getUserId());

        return "redirect:/findAllUser";
    }

    public String updateUser(UserUpdateDto userUpdateDto, String actionUrl, BindingResult result,
                             RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {
            checkValidation(result, model);
            modelForUser(model, userUpdateDto, true, actionUrl, userId);

            return "user-form";
        }

        User oUser = findUserByUserId(userUpdateDto.getUserId());
        oUser.setUsername(userUpdateDto.getUsername());
        oUser.setEmail(userUpdateDto.getEmail());

        oUser.deleteRole();
        oUser.setRoles(userUpdateDto.getRoles());
        oUser.deletePhoto();

        try {
            oUser.setPhoto(userUpdateDto.getPhoto().getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {

            userDao.saveAndFlush(oUser);
            attributes.addFlashAttribute("updateSuccess", oUser.getUserId());

            return "redirect:/findAllUser";

        } catch (DataIntegrityViolationException e) {

            result.addError(new FieldError("userDto", "email", "Email is taken"));
            checkValidation(result, model);

            modelForUser(model, userUpdateDto, true, actionUrl, userId);

            return "user-form";
        }
    }

    public String changePassword(String oldPassword, String password, String confirmPassword, Model model,
                                 RedirectAttributes attributes) {

        if (pswCheckValidation(oldPassword, password, confirmPassword, model)) {

            modelForUser(model, UserService.loginUser, true, "/changePsw", "");
            return "profile";

        }

        User oUser = findUserByUserId(loginUser.getUserId());
        oUser.setPassword(passwordEncoder.encode(password));
        userDao.saveAndFlush(oUser);

        modelForUser(model, UserService.loginUser, false, "/changePsw", "");
        attributes.addFlashAttribute("successChangePsw", true);

        return "redirect:/";
    }

    private boolean pswCheckValidation(String oldPassword, String password, String confirmPassword, Model model) {

        boolean valid = false;

        if (oldPassword.isEmpty()) {

            model.addAttribute("oldPswBlank", "Please fill in old password field");
            valid = true;

        } else if (!passwordEncoder.matches(oldPassword, loginUser.getPassword())) {

            model.addAttribute("oldPswWrong", "Current password is wrong");
            valid = true;

        }
        if (password.isEmpty() || confirmPassword.isEmpty()) {

            model.addAttribute("pswBlank", "Please fill in all the fields.");
            valid = true;

        }

        if (!password.equals(confirmPassword)) {

            model.addAttribute("pswNoMatch", "Passwords do not match.");
            valid = true;

        }
        return valid;
    }

    public List<User> findAllUser() {
        return userDao.findAll();
    }

    private <T> void modelForUser(Model model, T userDto, boolean oldUser, String link, String oUserId) {

        model.addAttribute("oldUser", oldUser);
        model.addAttribute("user", userDto);

//        Role role = roleDao.findById(1).get();
        Role role = roleDao.findRoleByName("ADMIN").get();
        Role role1 = roleDao.findRoleByName("SUPER_ADMIN").get();

        if (loginUser.getRoles().contains(role) | loginUser.getRoles().contains(role1)) {

        	List<Role> roles = roleDao.findAll().stream().filter(r ->	 !r.getName().equals("SUPER_ADMIN")).collect(Collectors.toList());
            model.addAttribute("role", roles);

        } else {

            List<Role> roles = roleDao.findAll().stream().filter(r -> !r.getName().equals("ADMIN") && !r.getName().equals("SUPER_ADMIN")).collect(Collectors.toList());
            model.addAttribute("role", roles);

        }
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
        return "user-form";
    }

    public String getUpdateForm(String userId, String actionUrl, Model model) {

        User user = findUserByUserId(userId);
        userId = user.getUserId();

        if (user.getPhoto() != null && user.getPhoto().length > 1) {

            String photo = Base64.getEncoder().encodeToString(user.getPhoto());
            model.addAttribute("dPhoto", photo);
        }

        modelForUser(model, UserUpdateDto.form(user), true, actionUrl, userId);

        return "user-form";

    }

    public String getProfileUpdateForm(String actionUrl, Model model) {

        User user = loginUser;
        userId = user.getUserId();
        modelForUser(model, UserUpdateDto.form(user), true, actionUrl, userId);

        return "user-form";
    }

    public String getDataForProfile(Model model) {

        model.addAttribute("id", loginUser.getId());
        modelForUser(model, loginUser, false, "/changePsw", "");

        return "profile";
    }

    public String getPswForm(Model model) {

        modelForUser(model, loginUser, true, "/changePsw", "");

        return "profile";
    }

    public String login(Model model) {

        return "login";
    }

    public List<User> searchUserAscSorting(String searchTerm, int page, int size, String column) {

        List<User> userList = userDao.findAll(SpecificationUtil.userWithSearchTerm(searchTerm),
                PageRequest.of(page, size, Sort.by(column).ascending())).getContent();
        
        userList = userList.stream().filter(u -> u.getId() != 1).collect(Collectors.toList());
        
        return userList;
    }

    public List<User> searchUserDescSorting(String searchTerm, int page, int size, String column) {

        List<User> userList = userDao.findAll(SpecificationUtil.userWithSearchTerm(searchTerm),
                PageRequest.of(page, size, Sort.by(column).descending())).getContent();
        
        userList = userList.stream().filter(u -> u.getId() != 1).collect(Collectors.toList());

        return userList;
    }

    @Transactional
    public boolean searchUserWithEmail(String email, Model model, RedirectAttributes attributes) {

        try {
            String otp = otpGenerator.generateOtp();

            User user = userDao.findUserByEmail(email).get();
            user.setOtp(otp);
            userDao.saveAndFlush(user);

            model.addAttribute("user", user);
            attributes.addFlashAttribute("foundUser", true);

            holder.otpHolder.put(otp, LocalTime.now());
            sendOtp(otp, user.getEmail());

            return true;
        } catch (Exception e) {
            attributes.addFlashAttribute("noUser", true);
            return false;
        }

    }

    public void sendOtp(String otp, String email) {
        try {
            User user = userDao.findUserByEmail(email).get();

            String message = "Dear " + user.getUsername() + ",\n"
                    + "We have received a request to reset your account password.\n"
                    + "Your code is: \t" + otp + ".\n"
                    + "Please use this code to reset your password. Do not share this code with anyone, as it provides access to your account."
                    + "This code is valid during 3minutes. "
                    + "If you did not request a password reset, please ignore this email.\n\n" + "Thank you";

            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setFrom("nanibaka.ushi@gmail.com");
            helper.setTo(email);
            helper.setText(message);
            helper.setSubject("Forgot Password");

            javaMailSender.send(mailMessage);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String checkOtp(String email, String otp, Model model, RedirectAttributes attributes) {

        User user = userDao.findUserByEmail(email).get();
        LocalTime lt = LocalTime.now().minusMinutes(3);

        if (user != null && user.getOtp().equals(otp)) {

            if (lt.isAfter(holder.otpHolder.get(otp))) {

                user.setOtp(null);
                attributes.addFlashAttribute("expireOtp", true);

                return "redirect:/expiredOtp";
            }

            validOtpForModel = true;
            model.addAttribute("validOtp", true);

            return "redirect:/changePassword";

        } else {

            attributes.addFlashAttribute("invalidOtp", true);
            model.addAttribute("validOtp", false);

            return "redirect:/invalidOtp";

        }
    }

    private boolean validOtpForModel;

    public boolean isValidOtpForModel() {

        return this.validOtpForModel;
    }

    public User findUserByEmail(String email) {

        return userDao.findUserByEmail(email).get();
    }

    public void forgotPasswordChange(String password, String email, boolean validOtp, RedirectAttributes attributes)
            throws Exception {
        if (!validOtp) {
            throw new Exception("Something's wrong");
        }

        validOtpForModel = false;

        User user = findUserByEmail(email);
        String dePassword = decryptPassword.decryptPassword(password);

        user.setPassword(passwordEncoder.encode(dePassword));
        user.setOtp(null);
        userDao.saveAndFlush(user);

        attributes.addFlashAttribute("forgotPswSuccess", true);
    }
}
