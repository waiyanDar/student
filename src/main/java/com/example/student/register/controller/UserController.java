package com.example.student.register.controller;

import com.example.student.register.dto.UserDto;
import com.example.student.register.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.student.register.service.UserService;
import com.example.student.register.service.RoleService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/registerUser")
	public String registerForm(Model model) {
		model.addAttribute("user", new UserDto());
		model.addAttribute("role", roleService.findAllRole());
		return "userRegisterForm";
	}

	@PostMapping("/registerUser")
	public String registerUser(@Valid UserDto userDto, BindingResult result, RedirectAttributes attributes) {

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
			return //
			"redirect:/registerUser";
		}

		if (result.hasErrors()) {
			return "redirect:/registerUser";
		}
		User user = User.form(userDto);
		userService.registerUser(user, userDto.getRole().getId());
		return "redirect:/findAllUser";
	}

//	@PostMapping("/addRole")
//	public ResponseEntity<?> addRole(@RequestBody Role role) {
//		roleService.saveRole(role);
//		return ResponseEntity.status(HttpStatus.OK).body(role);
//	}

//	@GetMapping("/findUser")
//	public String findUser(@RequestParam("id") int id) {
//
////		return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdmin(id));
//		return "hello";
//
//	 }

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
	public String deleteUser(@RequestParam("id") int id) {

		userService.deleteUser(id);

		return "redirect:/findAllUser";
	}

	String oUserId;
	int oId;

	@GetMapping("/userUpdate")
	public String uiChange(@RequestParam("id") int id, Model model) {
		User oUser = userService.findUser(id);
		oUserId = oUser.getUserId();
		oId = oUser.getId();
		model.addAttribute("oUser", UserDto.form(oUser));
		model.addAttribute("role", roleService.findAllRole());
		return "userUpdateForm";
	}

	@PostMapping("/userUpdate")
	public String updateAdmin(UserDto userDto, RedirectAttributes attributes, BindingResult result) {
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
		userService.updateUser(user, userDto.getRole().getId());
		return "redirect:/findAllUser";
	}

	@GetMapping("/findAllUser")
	public String findAllAdmin(Model model) {

		model.addAttribute("userList", userService.findAllUser());

		return "userList";
	}

}
