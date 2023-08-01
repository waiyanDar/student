package com.example.student.register.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
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

    @Autowired
    private UserActionDao userActionDao;

    private SecurityContextHolder contextHolder;

    //	@After("execution( * com.example.student.register.controller.UserController.*(..))")
    public void testing(JoinPoint joinPoint) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        UserAction userAction = userActionDao.findUserActionByUserId(userId).get();

        boolean firstTime = false;

        if (userAction == null) {

            userAction = new UserAction();
            userAction.setUserId(userId);
            firstTime = true;
        }

        userAction.getAction().add(joinPoint.getSignature().getName());

        if (firstTime) {
            userActionDao.save(userAction);
            firstTime = false;
        } else {
            userActionDao.saveAndFlush(userAction);
        }

        System.out.println(userId + " did " + joinPoint.getSignature().getName());
    }

}
