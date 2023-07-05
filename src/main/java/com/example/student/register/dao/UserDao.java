package com.example.student.register.dao;

import com.example.student.register.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {

    List<User> findAllBy(Specification<User> specification);
}
