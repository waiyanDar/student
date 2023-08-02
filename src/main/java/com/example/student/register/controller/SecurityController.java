package com.example.student.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.student.register.service.UserService;

import lombok.Data;

@Controller
@Data
public class SecurityController {
	
	@Autowired
	private UserService userService;
	
	 @GetMapping("/login")
	    public String login(Model model) {

	        return "login";
	    }

	    @GetMapping("/login-error")
	    public String loginError(RedirectAttributes attributes) {

	        attributes.addFlashAttribute("loginError", true);

	        return "redirect:/login";
	    }

	    @GetMapping("/logout")
	    public String logout() {
	        return "redirect:/login";
	    }
	    
	    @GetMapping("/forgotPsw")
	    public String forgotPswForm(Model model) {
	        model.addAttribute("validOtp", false);
	        model.addAttribute("emial", "");
	        return "forgot-Psw";
	    }

	    @PostMapping("/emailForForgotPsw")
	    public String emailForForgotPsw(@RequestParam("email") String email, Model model, RedirectAttributes attributes) {
	        emailForForgotPsw = email;
	        model.addAttribute("validOtp", false);
	        if (userService.searchUserWithEmail(email, model, attributes)) {

	            return "redirect:/foundUser";
	        } else {
	            return "redirect:/forgotPsw";
	        }
	    }

	    String emailForForgotPsw;

	    @GetMapping("/foundUser")
	    public String foundUser(Model model) {

	        model.addAttribute("validOtp", false);
	        model.addAttribute("user", userService.findUserByEmail(emailForForgotPsw));
	        return "forgot-Psw";

	    }

	    @PostMapping("/checkOtp")
	    public String checkOtp(@RequestParam("otp") String otp, @RequestParam("email") String email,
	                           RedirectAttributes attributes, Model model) {

	        emailForForgotPsw = email;
	        return userService.checkOtp(email, otp, model, attributes);

	    }

	    @GetMapping("/expiredOtp")
	    public String expiredOtp(Model model) {

	        model.addAttribute("validOtp", false);
	        model.addAttribute("email", emailForForgotPsw);
	        return "forgot-Psw";

	    }

	    boolean validOtp;

	    @GetMapping("/invalidOtp")
	    public String invalidOtp(Model model) {

	        model.addAttribute("validOtp", false);
	        model.addAttribute("user", userService.findUserByEmail(emailForForgotPsw));
	        return "forgot-Psw";

	    }

	    @GetMapping("/changePassword")
	    public String getChangePasswordForm(Model model) throws Exception {

	        validOtp = userService.isValidOtpForModel();

	        if (!validOtp) {
	            throw new Exception("Something's wrong");
	        }
	        model.addAttribute("validOtp", true);
	        return "forgot-Psw";
	    }

	    @PostMapping("/forgotPasswordChange")
	    public String forgotPasswordChange(@RequestParam("password") String password, RedirectAttributes attributes)
	            throws Exception {

	        userService.forgotPasswordChange(password, emailForForgotPsw, validOtp, attributes);
	        emailForForgotPsw = null;
	        return "redirect:/login";

	    }

}
