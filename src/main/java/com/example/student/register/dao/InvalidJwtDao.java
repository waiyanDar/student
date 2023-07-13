package com.example.student.register.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.example.student.register.entity.InvalidJwt;

public interface InvalidJwtDao extends JpaRepositoryImplementation<InvalidJwt, Integer>{

}
