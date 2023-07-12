package com.example.student.register.controller;

import java.time.LocalDate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.student.register.entity.Course;
import com.example.student.register.security.annotation.Admin;
import com.example.student.register.service.CourseService;

@Controller
public class CourseController {

    private CourseService courseService;
    
    public CourseController(CourseService courseService) {
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
    public String addCourse(@Valid Course course, BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            return "course-form";
        }
       
        
        try {
        	courseService.addCourse(course);
        	model.addAttribute("course", new Course());
            return "course-form";
		} catch (DataIntegrityViolationException e) {
//			result.addError(new FieldError("course", "name", "Course is already exist"));
			model.addAttribute("duplicateCourse", course.getName());
			model.addAttribute("course", new Course());
			return "course-form";
		}
    }

    @GetMapping("/")
    public String goHome(HttpServletRequest request, HttpServletResponse response) {
    	
        return "home";
    }

}
