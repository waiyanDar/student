package com.example.student.register.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.student.register.entity.Course;

public interface CourseDao extends JpaRepository<Course, String>{

}
