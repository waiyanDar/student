package com.example.student.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.InvalidJwtDao;
import com.example.student.register.entity.InvalidJwt;

@Service
public class InvalidJwtService {

	@Autowired
	private InvalidJwtDao invalidJwtDao;
	
	public void saveToken(String token) {
		System.out.println(token);
		InvalidJwt invalidJwt = new InvalidJwt();
		invalidJwt.setToken(token);
		invalidJwtDao.save(invalidJwt);
	}
}
