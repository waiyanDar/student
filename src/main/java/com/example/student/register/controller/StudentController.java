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

import javax.validation.Valid;


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

    String studentId;
    @ModelAttribute("studentId")
    public String studentId(){
        return studentId;
    }
    @GetMapping("/registerStudent")
    @Admin
    public String getStudentRegisterForm(Model model) {

        model.addAttribute("oldStu", false);
        model.addAttribute("student", new Student());
    	model.addAttribute("genders", Student.Gender.values());
    	model.addAttribute("educations" , Student.Education.values());
    	model.addAttribute("courses", courseService.findAllCourse());
        model.addAttribute("actionUrlForStu", "/registerStudent");
    	return "studentRegisterForm";
    }

    @PostMapping("/registerStudent")
    @Admin
    public String registerStudent(@Valid Student student, BindingResult result, Model model, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute("oldStu", false);
        	model.addAttribute("genders", Student.Gender.values());
        	model.addAttribute("educations" , Student.Education.values());
        	model.addAttribute("courses", courseService.findAllCourse());
            return "studentRegisterForm";
        }
        studentService.registerStudent(student);
        studentId = student.getStudentId();
        attributes.addFlashAttribute("stuAdd", true);
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
        model.addAttribute("oldStu", true);
        model.addAttribute("student",oStudent);
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("educations" , Student.Education.values());
        model.addAttribute("courses", courseService.findAllCourse());
        model.addAttribute("actionUrlForStu","/updateStudent" );
        return "studentRegisterForm";
    }

    @PostMapping("/updateStudent")
    @Admin
    public String updateStudent(@Valid Student student, BindingResult result,RedirectAttributes attributes,Model model) {
        if (result.hasErrors()){
            return "redirect:/seeMore?id="+oId;
        }
        student.setId(oId);
        student.setStudentId(oStudentId);
        studentService.updateStudent(student);
        studentId = student.getStudentId();
        attributes.addFlashAttribute("stuUpdate", true);
        return "redirect:/findAllStudent";
    }

    @GetMapping("/findAllStudent")
    @Admin
    public String findAllStudent(Model model) {
    	
         model.addAttribute("students", studentService.findAllStudent());
         
         return "studentList";
    }

    @GetMapping("/deleteStudent")
    @Admin
    public String deleteStudent(@RequestParam("id") int id, RedirectAttributes attributes) {

        studentId = studentService.deleteStudent(id);
        attributes.addFlashAttribute("stuDelete", true);
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

    boolean oldStu;
//    @ModelAttribute("oldStu")
    public boolean isOldStu(){
        return oldStu;
    }
}
