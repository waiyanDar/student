package com.example.student.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.student.register.dao.RoleDao;
import com.example.student.register.entity.Admin;
import com.example.student.register.service.AdminService;
import com.example.student.register.service.RoleService;

@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleDao dao;

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("admin", new Admin());
		model.addAttribute("role",roleService.findAllRole());
		return "adminForm";
	}
	@PostMapping("/register")
	public String register( @Validated Admin admin, BindingResult result) {
		if (result.hasErrors()){
			return "adminForm";
		}
		adminService.register(admin);
		return "redirect:/findAllAdmin";
	}

//	@PostMapping("/addRole")
//	public ResponseEntity<?> addRole(@RequestBody Role role) {
//		roleService.saveRole(role);
//		return ResponseEntity.status(HttpStatus.OK).body(role);
//	}

	@GetMapping("/getAdmin") 
	public String getAdmin(@RequestParam("id") int id) {
	 
//		return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdmin(id)); 
		return "hello";
	 
	 }
	
	@GetMapping("/delete")
	public String deleteAdmin(@RequestParam("id") int id) {
		
		adminService.deleteAdmin(id);
		
		return "redirect:/findAllAdmin";
	}
	
	String oAdminId;
	int oId;
	
	@GetMapping("/adminForm")
	public String uiChange(@RequestParam("id")int id, Model model) {
		Admin oAdmin = adminService.getAdmin(id);
		oAdminId = oAdmin.getAdminId();
		oId = oAdmin.getId();
		model.addAttribute("oAdmin",oAdmin);
		model.addAttribute("role", roleService.findAllRole());
		return "adminUploadForm";
	}

	
	@PostMapping("/update")
	public String updateAdmin(@Validated Admin admin, BindingResult result,RedirectAttributes redirectAttributes){
		if (result.hasErrors()) {
			return "redirect:/adminForm";
		}
		admin.setId(oId);
		admin.setAdminId(oAdminId);
		adminService.updateAdmin(admin);
		return "redirect:/findAllAdmin";
	}
	
	
	@GetMapping("/findAllAdmin")
	public String findAllAdmin(Model model){
		
		model.addAttribute("adminList", adminService.findAllAdmin());
		
		return "adminList";
	}
	 
}
