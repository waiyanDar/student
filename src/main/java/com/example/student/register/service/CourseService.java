package com.example.student.register.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.CourseDao;
import com.example.student.register.entity.Course;

@Service
public class CourseService {
	
	@Autowired
	private CourseDao courseDao;
	
	public Course addCourse(Course course) {
		
		return courseDao.save(course);
	}
	
	public List<Course> findAllCourse(){
		
		return courseDao.findAll();
	}

}
