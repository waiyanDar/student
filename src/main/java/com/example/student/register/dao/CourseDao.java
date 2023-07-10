package com.example.student.register.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.example.student.register.entity.Course;

public interface CourseDao extends JpaRepositoryImplementation<Course, Integer> {

}
