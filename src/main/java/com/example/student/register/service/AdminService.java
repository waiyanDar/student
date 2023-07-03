package com.example.student.register.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.student.register.dao.AdminDao;
import com.example.student.register.dao.RoleDao;
import com.example.student.register.entity.Admin;
import com.example.student.register.entity.Role;

@Service
public class AdminService {

	
	private final AdminDao adminDao;
	
	private final RoleDao roleDao;
	
	public AdminService(AdminDao adminDao,RoleDao roleDao) {
		this.adminDao = adminDao;
		this.roleDao = roleDao;
	}
	
	public void register(Admin admin) {
		int nextId = findAllAdmin().size()+1;
		String formattedId = String.format("USR%03d",nextId);
		admin.setAdminId(formattedId);
		Role role = roleDao.findByName(admin.getRole().getName()).get();
		admin.setRole(role);
		adminDao.save(admin);
	}
	
	public Admin getAdmin(int id) {
		return adminDao.findById(id).get();
	}
	
	public void deleteAdmin(int id) {
		Admin admin = getAdmin(id);
		adminDao.delete(admin);
	}
	
	public Admin updateAdmin(Admin admin) {
		
		Admin oAdmin = getAdmin(admin.getId());
		oAdmin.setUsername(admin.getUsername());
		oAdmin.setEmail(admin.getEmail());
		oAdmin.setPassword(admin.getPassword());
		oAdmin.setRole(roleDao.findByName(admin.getRole().getName()).get());
		
		return adminDao.saveAndFlush(oAdmin);
	}
	
	public List<Admin> findAllAdmin(){
		return adminDao.findAll();
	}
}
