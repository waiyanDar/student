package com.example.student.register.service;

import java.util.List;
import java.util.Optional;

import static com.example.student.register.util.SpecificationUtil.*;

import com.example.student.register.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.student.register.dao.UserDao;
import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dao.RoleDao;
import com.example.student.register.entity.Role;

@Service
public class UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;
    
	private PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user, List<Role> roles) {
    	user.setRoles(roles);
    	
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);

    }

    public User findUserById(int id) {
        return userDao.findById(id).get();
    }
    
    public static User loginUser;
    
    public User findUserByUserId(String userId) {
    	loginUser =  userDao.findUserByUserId(userId).get();
    	return loginUser;
    }

    public String deleteUser(int id) {
        User user = findUserById(id);
        String userId = user.getUserId();
        userDao.delete(user);
        return userId;
    }

    public User updateUser(User user, List<Role> roles) {

        User oUser = findUserById(user.getId());
        oUser.setUsername(user.getUsername());
        oUser.setEmail(user.getEmail());
//        oUser.setPassword(passwordEncoder.encode(user.getPassword()));

        oUser.deleteRole();
        oUser.setRoles(roles);
        return userDao.saveAndFlush(oUser);
    }
    
    public User changePassword(String password, String confirmPassword, BindingResult result, Model model) {
    	
    	if(!password.equals(confirmPassword)) {
    		result.addError(new FieldError("password", "password", "Password cannot be blank"));
    	}
    	if (result.hasFieldErrors("password")){
            model.addAttribute("invalidPassword", result.getFieldError("password").getDefaultMessage());
        }
        if (result.hasFieldErrors("confirmPassword")){
            model.addAttribute("invalidCPsw", result.getFieldError("confirmPassword").getDefaultMessage());
        }
    	User oUser = findUserByUserId(loginUser.getUserId());
    	oUser.setPassword(passwordEncoder.encode(password));
    	
    	return userDao.saveAndFlush(oUser);
    }

    public List<User> findAllUser() {
        return userDao.findAll();
    }

    public List<User> searchUser(Optional<String> userId, Optional<String> name) {
        Specification<User> specification = withUserId(userId).and(withName(name));
        return userDao.findAll(specification);
    }
}
