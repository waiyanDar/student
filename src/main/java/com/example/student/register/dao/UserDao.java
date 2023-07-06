package com.example.student.register.dao;

import com.example.student.register.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;


public interface UserDao extends JpaRepositoryImplementation<User, Integer> {

	Optional<User> findUserByUserId(String userId);
}
