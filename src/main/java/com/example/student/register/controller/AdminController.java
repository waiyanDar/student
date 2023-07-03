package com.example.student.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.register.dao.RoleDao;
import com.example.student.register.entity.Admin;
import com.example.student.register.entity.Role;
import com.example.student.register.service.AdminService;
import com.example.student.register.service.RoleService;

@RestController
@RequestMapping("/api/v1")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleDao dao;

	@PostMapping("/register")
	public String register(@RequestBody @Validated Admin admin, BindingResult result) {
		if (result.hasErrors()){
			String errorMessage = "";
			for (FieldError error : result.getFieldErrors()) {
				errorMessage += error.getDefaultMessage() + "; ";
			}
			return errorMessage;
		}
		adminService.register(admin);
		return "successfully uploaded";
	}

	@PostMapping("/addRole")
	public ResponseEntity<?> addRole(@RequestBody Role role) {
		roleService.saveRole(role);
		return ResponseEntity.status(HttpStatus.OK).body(role);
	}

	@GetMapping("/getAdmin") 
	public ResponseEntity<?> getAdmin(@RequestParam("id") int id) {
	 
		return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdmin(id)); 
	 
	 }
	
	@DeleteMapping("/delete")
	public String deleteAdmin(@RequestParam("id") int id) {
		
		adminService.deleteAdmin(id);
		
		return "successfully deleted";
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateAdmin(@RequestBody  Admin admin){
		
		return ResponseEntity.status(HttpStatus.OK).body(adminService.updateAdmin(admin));
	}
	
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllAdmin(){
		
		return ResponseEntity.status(HttpStatus.OK).body(adminService.findAllAdmin());
	}
	 
}
