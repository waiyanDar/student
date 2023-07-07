package com.example.student.register;

import com.example.student.register.entity.Role;
import com.example.student.register.entity.User;
import com.example.student.register.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.example.student.register.dao.RoleDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class StudentRegisterApplication {

    private final RoleDao roleDao;

    private final UserService userService;

    public StudentRegisterApplication(RoleDao roleDao, UserService userService) {
        this.roleDao = roleDao;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(StudentRegisterApplication.class, args);
    }


//    @Override
//    public void run(String... args) throws Exception {
//		Role role1 = new Role();
//		role1.setName("ADMIN");
//		Role role2 = new Role();
//		role2.setName("USER");
//		roleDao.save(role1);
//		roleDao.save(role2);
//    }

    @Bean
    @Transactional
    @Profile("Test")
    public CommandLineRunner runner(){
        return args -> {
            Role role1 = new Role();
            role1.setName("ADMIN");
            Role role2 = new Role();
            role2.setName("USER");
            roleDao.save(role1);
            roleDao.save(role2);

            User user = new User();
            user.setUsername("waiyan");
            user.setEmail("waiyan@gmail.com");
            user.setPassword("12345");

            userService.registerUser(user, Arrays.asList(role1,role2));
        };
    }
}
