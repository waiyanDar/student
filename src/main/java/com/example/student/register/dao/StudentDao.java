package com.example.student.register.dao;

import com.example.student.register.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDao extends JpaRepository<Student, Integer> {
}
