package com.example.student.register.controller;

import com.example.student.register.entity.Student;
import com.example.student.register.security.annotation.Admin;
import com.example.student.register.service.CourseService;
import com.example.student.register.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class StudentController {

    private StudentService studentService;
    
    private CourseService courseService;
    
    public StudentController(StudentService studentService, CourseService courseService) {
    	this.studentService = studentService;
    	this.courseService = courseService;
    }
    
    @ModelAttribute("loginDate")
	public String loginDate() {
		return LocalDate.now().toString();
	}
    
    @GetMapping("/registerStudent")
    @Admin
    public String getStudentRegisterForm(Model model) {
    	model.addAttribute("student", new Student());
    	model.addAttribute("genders", Student.Gender.values());
    	model.addAttribute("educations" , Student.Education.values());
    	model.addAttribute("courses", courseService.findAllCourse());
    	return "studentRegisterForm";
    }

    @PostMapping("/registerStudent")
    @Admin
    public String registerStudent(Student student, BindingResult result, Model model, RedirectAttributes attributes) {
    	String phoneRegex = "[0-9]*";
    	Pattern pattern = Pattern.compile(phoneRegex);
    	Matcher matcher = pattern.matcher(student.getPhone());
    	boolean phoneIsValid = matcher.matches();
        if (student.getName() == null || student.getName().isEmpty()) {
            attributes.addFlashAttribute("nameIsNull", true);
            return "redirect:/registerStudent";
        }
        if (student.getPhone() == null || student.getPhone().isEmpty()) {
            attributes.addFlashAttribute("phoneIsNull", true);
            return "redirect:/registerStudent";
        }
        if (student.getDateOfBirth() == null || student.getDateOfBirth().isEmpty()) {
            attributes.addFlashAttribute("dobIsNull", true);
            return "redirect:/registerStudent";
        } 
        if (student.getGender() == null || student.getGender().name().isEmpty()) {
            attributes.addFlashAttribute("genderIsNull", true);
            return "redirect:/registerStudent";
        } 
        if (student.getEducation() == null || student.getEducation().name().isEmpty()) {
            attributes.addFlashAttribute("eduIsNull", true);
            return "redirect:/registerStudent";
        } 
        if(!phoneIsValid) {
        	attributes.addFlashAttribute("invalidPh", true);
        	return "redirect:/registerStudent";
        }
        if (student.getCourses() == null || student.getCourses().isEmpty()) {
            attributes.addFlashAttribute("courseIsNull", true);
            return "redirect:/registerStudent";
        } 
        
        if (result.hasErrors()) {
        	model.addAttribute("genders", Student.Gender.values());
        	model.addAttribute("educations" , Student.Education.values());
        	model.addAttribute("courses", courseService.findAllCourse());
            return "studentRegisterForm";
        }
        studentService.registerStudent(student);
        return "redirect:/findAllStudent";
    }

    @GetMapping("/getStudent")
    @Admin
    public String getStudent(@RequestParam("id") int id) {
        return studentService.findStudent(id).toString();
    }

    @GetMapping("/findAllStudent")
    @Admin
    public String findAllStudent(Model model) {
    	
         model.addAttribute("students", studentService.findAllStudent());
         
         return "studentList";
    }

    @GetMapping("/deleteStudent")
    @Admin
    public String deleteStudent(@RequestParam("id") int id) {
        System.out.println(id);
        studentService.deleteStudent(id);
        return "redirect:/findAllStudent";
    }

    int oId;
    String oStudentId;
    
    @GetMapping("/seeMore")
    @Admin
    public String studentInfo(@RequestParam("id") int id, Model model) {
    	Student oStudent = studentService.findStudent(id);
    	oId = oStudent.getId();
    	oStudentId = oStudent.getStudentId();
    	model.addAttribute("oStudent",oStudent);
    	model.addAttribute("genders", Student.Gender.values());
    	model.addAttribute("educations" , Student.Education.values());
    	model.addAttribute("courses", courseService.findAllCourse());
    	return "studentInfo";
    }
    
    @PostMapping("/updateStudent")
    @Admin
    public String updateStudent(Student student, BindingResult result,RedirectAttributes attributes) {
    	student.setId(oId);
    	student.setStudentId(oStudentId);
    	studentService.updateStudent(student);
    	
    	return "redirect:/findAllStudent";
    }
    
    @GetMapping("/searchStudent")
    @Admin
    public String searchStudent(@RequestParam("studentId") Optional<String> studentId,
    							@RequestParam("studentName") Optional<String> studentName,
    							@RequestParam("courseName") Optional<String> courseName, Model model) {
    	
    	List<Student> students = studentService.searchStudent(studentId, studentName, courseName);
    	model.addAttribute("students", students);
    	model.addAttribute("studentId", studentId.orElse(""));
    	model.addAttribute("studentName", studentName.orElse(""));
    	model.addAttribute("courseName", courseName.orElse(""));
    	return "studentList";
    }

}
