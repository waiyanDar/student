package com.example.student.register;

import com.example.student.register.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import com.example.student.register.dao.RoleDao;

@SpringBootApplication
public class StudentRegisterApplication implements CommandLineRunner {

    @Autowired
    private RoleDao roleDao;


    public static void main(String[] args) {
        SpringApplication.run(StudentRegisterApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
		Role role1 = new Role();
		role1.setName("ADMIN");
		Role role2 = new Role();
		role2.setName("STUDENT");
//		roleDao.save(role1);
//		roleDao.save(role2);
    }

}
