package com.example.student.register;

import com.example.student.register.entity.Role;
import com.example.student.register.entity.User;
import com.example.student.register.service.UserService;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.example.student.register.dao.RoleDao;

import static com.example.student.register.util.RolesForSecurity.*;

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
    
    @Bean
    @Transactional
    @Profile("Test")
    public CommandLineRunner runner () {
    	return arg -> {
    		Role role1 = new Role();
    		role1.setName(ROLES_ADMIN);
    		Role role2 = new Role();
    		role2.setName(USER_ADMIN);
    		Role role3 = new Role();
    		role3.setName(USER_CREATE);
    		Role role4 = new Role();
    		role4.setName(USER_UPDATE);
    		Role role5 = new Role();
    		role5.setName(USER_DELETE);
    		Role role6 = new Role();
    		role6.setName(USER_READ);
    		
    		roleDao.save(role1);
    		roleDao.save(role2);
    		roleDao.save(role3);
    		roleDao.save(role4);
    		roleDao.save(role5);
    		roleDao.save(role6);

    		User user = new User();
    		user.setUsername("wai yan");
    		user.setPassword("12345");
    		user.setEmail("waiyan@gmail.com");
    		userService.registerUser(user, Arrays.asList(role1, role2));
    	};
    }

}
