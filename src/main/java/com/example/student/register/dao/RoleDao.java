package com.example.student.register.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.example.student.register.entity.Role;

@Repository
public interface RoleDao extends JpaRepositoryImplementation<Role, Integer> {
	
	Optional<Role> findRoleByName(String name);
}
