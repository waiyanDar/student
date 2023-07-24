package com.example.student.register.controller;

import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.entity.User;
import com.example.student.register.explorter.UserExplorer;
import com.example.student.register.security.annotation.Admin;
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

	private final UserExplorer userExplorer;

	public UserController(UserService userService, UserExplorer userExplorer) {
		this.userService = userService;
		this.userExplorer = userExplorer;

	}

//    private User loginUser = UserService.loginUser;

	@ModelAttribute("loginDate")
	public String loginDate() {
		return LocalDate.now().toString();
	}

	@ModelAttribute("userId")
	public String userId() {
		return userService.getUserId();
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
	public String uiChange(@RequestParam("userId") String userId, Model model) {

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

//        @GetMapping("/findAllUser")
	@UserRead
	public String findAllUser(Model model) {

		model.addAttribute("userList", userService.findAllUser());

		return "user-list";
	}

	@GetMapping("/deleteUser")
	@UserDelete
	public String deleteUser(@RequestParam("userId") String userId, RedirectAttributes attributes) {

		return userService.deleteUser(userId, attributes);
	}

	@GetMapping("/searchUser")
	public String searchUser(@RequestParam("userId") Optional<String> userId,
			@RequestParam("username") Optional<String> username, Model model) {
		List<User> user = userService.searchUser(userId, username);
		model.addAttribute("userList", user);
		model.addAttribute("searchUsername", username.orElse(""));
		return "user-list";
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

	@GetMapping("/login")
	public String login(Model model) {

		return userService.login(model);
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

	@GetMapping("/forgotPsw")
	public String forgotPsw(Model model) {
		model.addAttribute("validOtp", false);
		return "forgot-Psw";
	}

	@PostMapping("/checkOtp")
	public String checkOtp(@RequestParam("otp") String otp, @RequestParam("email") String email,
			RedirectAttributes attributes, Model model) {
		if (userService.checkOtp(email, otp)) {
			model.addAttribute("validOtp", true);
			return "forgot-Psw";
		} else {

			attributes.addFlashAttribute("invalidOtp", true);
			emailForForgotPsw = email;
			return "redirect:/invalidOtp";
		}

	}

	String emailForForgotPsw;

	@GetMapping("/invalidOtp")
	public String invalidOtp(Model model) {
		model.addAttribute("validOtp", false);
		model.addAttribute("user", userService.findUserByEmail(emailForForgotPsw));
		return "forgot-Psw";
	}

	@PostMapping("/emailForForgotPsw")
	public String emailForForgotPsw(@RequestParam("email") String email, Model model, RedirectAttributes attributes) {
		emailForForgotPsw = email;
		model.addAttribute("validOtp", false);
		validOtp = userService.searchUserWithEmail(email, model, attributes);
		if (validOtp) {
			return "redirect:/foundUser";
		} else {
			return "redirect:/forgotPsw";
		}
	}

	boolean validOtp;
	@GetMapping("/foundUser")
	public String foundUser(Model model) {
		model.addAttribute("validOtp", false);
		model.addAttribute("user", userService.findUserByEmail(emailForForgotPsw));
		return "forgot-Psw";
	}

}
