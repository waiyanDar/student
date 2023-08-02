package com.example.student.register.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import com.example.student.register.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.student.register.dto.CourseDto;
import com.example.student.register.security.annotation.Admin;
import com.example.student.register.service.CourseService;
import com.example.student.register.service.SmbService;

import lombok.Data;

@Controller
@Data
//@Aspect
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private SmbService smbservice;

    @ModelAttribute("loginDate")
    public String loginDate() {
        return LocalDate.now().toString();
    }

    @GetMapping("/addCourse")
    @Admin
    public String courseForm(Model model) {
        model.addAttribute("course", new CourseDto());
        return "course-form";
    }

    @PostMapping("/addCourse")
    @Admin
    public String addCourse(@Valid CourseDto courseDto, BindingResult result, RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {

            attributes.addFlashAttribute("invalidName", result.getFieldError("name").getDefaultMessage());
            return "redirect:/addCourse";

        }

        try {

            courseService.addCourse(courseDto);
            attributes.addFlashAttribute("successCourse", courseDto.getName());
            return "redirect:/addCourse";

        } catch (DataIntegrityViolationException e) {

            attributes.addFlashAttribute("duplicateCourse", courseDto.getName());

            return "redirect:/addCourse";

        }
    }

    @GetMapping("/")
    public String goHome(Model model) {
        return "home";
    }

    @ModelAttribute("quartzDate")
    public String quartzDate(){
        return quartzService.getStrDate();
    }

    @ModelAttribute("quartzDay")
    public String quartzDay(){
        return quartzService.getStrDays();
    }

    @ModelAttribute("autoReport")
    public boolean isAutoReport(){
        return quartzService.isBoolAutoReport();
    }

    @ModelAttribute("quartzTime")
    public String quartzTime(){
        return quartzService.getStrTime();
    }

}
