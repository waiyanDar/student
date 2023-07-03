package com.example.student.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.register.entity.Course;
import com.example.student.register.service.CourseService;

@RestController
@RequestMapping("/api/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@PostMapping("/addCourse")
	public ResponseEntity<?> addCourse(@RequestBody Course course){
		
		return ResponseEntity.status(HttpStatus.OK).body(courseService.addCourse(course));
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAllCourse(){
		
		return ResponseEntity.status(HttpStatus.OK).body(courseService.findAllCourse());
	}
	
}
