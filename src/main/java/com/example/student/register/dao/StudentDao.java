package com.example.student.register.dao;

import com.example.student.register.entity.Student;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface StudentDao extends JpaRepositoryImplementation<Student, Integer> {
	
	Optional<Student> findStudentByStudentId(String studentId);
}
