package com.example.student.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.RoleDao;
import com.example.student.register.entity.Role;

@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;
	
	public void saveRole(Role role) {
		roleDao.save(role);
	}
}
