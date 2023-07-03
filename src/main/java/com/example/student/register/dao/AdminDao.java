package com.example.student.register.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.student.register.entity.Admin;

public interface AdminDao extends JpaRepository<Admin, Integer> {

}
