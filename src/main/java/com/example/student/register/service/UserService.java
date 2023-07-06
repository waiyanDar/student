package com.example.student.register.service;

import java.util.List;
import java.util.Optional;

import static com.example.student.register.util.SpecificationUtil.*;

import com.example.student.register.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.UserDao;
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

    public void registerUser(User user, int roleId) {
        Role role = roleDao.findById(roleId).get();
//		User user = User.form(userDto);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public User findUserById(int id) {
        return userDao.findById(id).get();
    }
    
    public User findUserByUserId(String userId) {
    	return userDao.findUserByUserId(userId).get();
    }

    public void deleteUser(int id) {
        User user = findUserById(id);
        userDao.delete(user);
    }

    public User updateUser(User user, int roleId) {

        User oUser = findUserById(user.getId());
        oUser.setUsername(user.getUsername());
        oUser.setEmail(user.getEmail());
        oUser.setPassword(user.getPassword());
        oUser.setRole(roleDao.findById(roleId).get());
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
