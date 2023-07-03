package com.example.student.register.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.student.register.entity.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByName(String name);

}
