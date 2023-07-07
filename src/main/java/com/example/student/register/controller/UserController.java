package com.example.student.register.controller;

import com.example.student.register.dto.UserDto;
import com.example.student.register.entity.User;
import com.example.student.register.security.annotation.UserAdmin;
import com.example.student.register.security.annotation.UserCreate;
import com.example.student.register.security.annotation.UserDelete;
import com.example.student.register.security.annotation.UserRead;
import com.example.student.register.security.annotation.UserUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
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

	private Authentication authentication;

	public UserController(UserService userService, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
	}

	/*
	 * @ModelAttribute("usernameAndUserId") public String loginUser() { User user=
	 * userService.findUserByUserId(authentication.getName()); return
	 * user.getUserId() + " " + user.getUsername(); }
	 */

	@ModelAttribute("loginDate")
	public String loginDate() {
		return LocalDate.now().toString();
	}

	@GetMapping("/registerUser")
	@UserAdmin
	@UserCreate
	public String registerForm(Model model) {
		model.addAttribute("user", new UserDto());
		model.addAttribute("role", roleService.findAllRole());
		return "userRegisterForm";
	}

	@PostMapping("/registerUser")
	@UserAdmin
	@UserCreate
	public String registerUser(@Valid UserDto userDto, BindingResult result, RedirectAttributes attributes,
			Model model) {

		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(userDto.getEmail());
		boolean isValidEmail = matcher.matches();

		if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
			attributes.addFlashAttribute("usernameIsNull", true);
			return "redirect:/registerUser";
		}
		if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
			attributes.addFlashAttribute("passwordIsNull", true);
			return "redirect:registerUser";
		}
		if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
			attributes.addFlashAttribute("passwordNotMatch", true);
			return "redirect:registerUser";
		}
		if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
			attributes.addFlashAttribute("emailIsNull", true);
			return "redirect:/registerUser";
		} else if (!isValidEmail) {
			attributes.addFlashAttribute("emailPatternInvalid", true);
			return "redirect:/registerUser";
		}

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

	@GetMapping("/searchUser")
	public String searchUser(@RequestParam("userId") Optional<String> userId,
			@RequestParam("username") Optional<String> username, Model model) {
		List<User> user = userService.searchUser(userId, username);
		model.addAttribute("userList", user);
		model.addAttribute("searchUserId", userId.orElse(""));
		model.addAttribute("searchUsername", username.orElse(""));
		return "userList";
	}

	
	@GetMapping("/deleteUser")
	@UserAdmin
	@UserUpdate
	public String deleteUser(@RequestParam("id") int id, RedirectAttributes attributes) {

		userService.deleteUser(id);
		attributes.addFlashAttribute("deleteUser", true);
		return "redirect:/findAllUser";
	}

	String oUserId;
	int oId;

	@GetMapping("/userUpdate")
	@UserAdmin
	@UserUpdate
	public String uiChange(@RequestParam("id") int id, Model model) {
		User oUser = userService.findUserById(id);
		oUserId = oUser.getUserId();
		oId = oUser.getId();
		model.addAttribute("oUser", UserDto.form(oUser));
		model.addAttribute("role", roleService.findAllRole());
		return "userUpdateForm";
	}

	@PostMapping("/userUpdate")
	@UserUpdate
	@UserAdmin
	public String updateAdmin(UserDto userDto, RedirectAttributes attributes, BindingResult result, Model model) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(userDto.getEmail());
		boolean isValidEmail = matcher.matches();
		if (result.hasErrors()) {
			return "redirect:/userUpdate?id=" + oId;
		}
		if (userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
			attributes.addFlashAttribute("usernameIsNull", true);
			return "redirect:/userUpdate?id=" + oId;
		}
		if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
			attributes.addFlashAttribute("passwordIsNull", true);
			return "redirect:/userUpdate?id=" + oId;
		}
		if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
			attributes.addFlashAttribute("passwordNotMatch", true);
			return "redirect:/userUpdate?id=" + oId;
		}
		if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
			attributes.addFlashAttribute("emailIsNull", true);
			return "redirect:/userUpdate?id=" + oId;
		} else if (!isValidEmail) {
			attributes.addFlashAttribute("emailPatternInvalid", true);
			return "redirect:/userUpdate?id=" + oId;
		}

		User user = User.form(userDto);
		user.setId(oId);
		user.setUserId(oUserId);
		try {
			userService.registerUser(user, userDto.getRoles());
			model.addAttribute("usrId", user.getId());
			attributes.addFlashAttribute("updateSuccess", true);
			return "redirect:/findAllUser";
		} catch (DataIntegrityViolationException e) {
			attributes.addFlashAttribute("emailDuplicate", true);
			return "redirect:/userUpdate?id=" + oId;
		}
		
	}

	@GetMapping("/findAllUser")
	@UserAdmin
	@UserDelete
	@UserUpdate
	@UserRead
	public String findAllAdmin(Model model) {

		model.addAttribute("userList", userService.findAllUser());

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
