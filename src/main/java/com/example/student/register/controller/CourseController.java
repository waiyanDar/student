package com.example.student.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.student.register.entity.Course;
import com.example.student.register.service.CourseService;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/addCourse")
    public String courseForm(Model model) {
        model.addAttribute("course", new Course());
        return "courseForm";
    }

    @PostMapping("/addCourse")
    public String addCourse(@Validated Course course, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "courseForm";
        }
        courseService.addCourse(course);
        attributes.addFlashAttribute("success", true);
        return "courseForm";
    }

    @GetMapping("/findAllCourse")
    public String findAllCourse(Model model) {
        model.addAttribute("courseList", courseService.findAllCourse());
        return "courseList";
    }

    @GetMapping("/")
    public String goHome() {
        return "Home";
    }

}
