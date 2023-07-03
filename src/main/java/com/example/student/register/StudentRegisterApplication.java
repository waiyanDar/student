package com.example.student.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.student.register.dao.RoleDao;
import com.example.student.register.entity.Role;
import com.example.student.register.service.AdminService;

@SpringBootApplication
public class StudentRegisterApplication implements CommandLineRunner {
	
	@Autowired
	private RoleDao roleDao;
	

	public static void main(String[] args) {
		SpringApplication.run(StudentRegisterApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
	
	}

}
