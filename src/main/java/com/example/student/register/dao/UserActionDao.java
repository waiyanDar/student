package com.example.student.register.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.student.register.entity.UserAction;

public interface UserActionDao extends JpaRepository<UserAction, Integer>{

	Optional<UserAction> findUserActionByUserId(String userId);
}
