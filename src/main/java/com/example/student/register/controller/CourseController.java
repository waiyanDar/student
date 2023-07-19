package com.example.student.register.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.student.register.entity.Course;
import com.example.student.register.security.annotation.Admin;
import com.example.student.register.service.CourseService;

@Controller
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService ) {
    	this.courseService = courseService;
    }
    
    @ModelAttribute("loginDate")
	public String loginDate() {
		return LocalDate.now().toString();
	}

    @GetMapping("/addCourse")
    @Admin
    public String courseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/addCourse")
    @Admin
    public String addCourse(@Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "course-form";
        }
       
        
        try {
        	courseService.addCourse(course);
        	model.addAttribute("course", new Course());
            return "course-form";
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("duplicateCourse", course.getName());
			model.addAttribute("course", new Course());
			return "course-form";
		}
    }

    @GetMapping("/")
    public String goHome() {
        return "home";
    }

}
