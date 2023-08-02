package com.example.student.register.service;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.UserActionDao;
import com.example.student.register.entity.UserAction;

import lombok.Data;

@Service
@Data
@Aspect
public class UserActionService {

	private final Logger errorLogger = LoggerFactory.getLogger("Error : ");

	@Autowired
	private UserActionDao userActionDao;

	@After("execution( * com.example.student.register.controller.UserController.*(..))")
	public void testing(JoinPoint joinPoint) {

		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		UserAction userAction = new UserAction();

		userAction.setUserId(userId);
		userAction.setAction(joinPoint.getSignature().getName());
		userAction.setDate(LocalDateTime.now().toString());
		userActionDao.save(userAction);


	}

}
