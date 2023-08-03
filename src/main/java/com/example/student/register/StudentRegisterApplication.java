package com.example.student.register;

import com.example.student.register.dao.UserDao;
import com.example.student.register.entity.Role;
import com.example.student.register.entity.User;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.student.register.dao.RoleDao;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.logging.Logger;

import static com.example.student.register.security.roleHierarchy.RolesForSecurity.*;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class StudentRegisterApplication {

    private final RoleDao roleDao;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    
    private static final Logger logger = Logger.getLogger(StudentRegisterApplication.class.getName());

    public StudentRegisterApplication(RoleDao roleDao, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(StudentRegisterApplication.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner runner() {
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

            try {

                roleDao.save(role1);
                roleDao.save(role2);
                roleDao.save(role3);
                roleDao.save(role4);
                roleDao.save(role5);
                roleDao.save(role6);


            } catch (Exception e) {
				logger.warning(e.getMessage());
            }

            try {
                User user = new User();
            
                user.setUsername("wai yan");
                user.setPassword(passwordEncoder.encode("12345"));
                user.setEmail("waiyan@gmail.com");
                user.setRoles(Arrays.asList(role1));
                
                userDao.save(user);
            } catch (Exception e) {
                logger.warning(e.getMessage());
            }


        };
    }
}
