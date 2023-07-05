package com.example.student.register.service;

import java.util.List;
import java.util.Optional;

import static com.example.student.register.util.SpecificationUtil.*;

import com.example.student.register.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.UserDao;
import com.example.student.register.dao.RoleDao;
import com.example.student.register.entity.Role;

@Service
public class UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    public UserService(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public void registerUser(User user, int roleId) {
        Role role = roleDao.findById(roleId).get();
//		User user = User.form(userDto);
        user.setRole(role);
        userDao.save(user);
    }

    public User findUser(int id) {
        return userDao.findById(id).get();
    }

    public void deleteUser(int id) {
        User user = findUser(id);
        userDao.delete(user);
    }

    public User updateUser(User user, int roleId) {

        User oUser = findUser(user.getId());
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
