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

    private final StudentService studentService;
    
    private final CourseService courseService;
        

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

    	modelForStu(model,new Student(), false, "/registerStudent", "");
    	return "student-form";
    }

    @PostMapping("/registerStudent")
    @Admin
    public String registerStudent(@Valid Student student, BindingResult result, Model model, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            modelForStu(model,student,false, "", "");
            return "student-form";
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
        modelForStu(model, oStudent, true, "/updateStudent", oStudentId);
        return "student-form";
    }

    @PostMapping("/updateStudent")
    @Admin
    public String updateStudent(@Valid Student student, BindingResult result,RedirectAttributes attributes,Model model) {
        if (result.hasErrors()){
            modelForStu(model, student, true, "/updateStudent", oStudentId);
            return "student-form";
        }
        student.setId(oId);
        student.setStudentId(oStudentId);
        studentService.updateStudent(student);
        studentId = student.getStudentId();
        attributes.addFlashAttribute("stuUpdate", true);
        return "redirect:/findAllStudent";
    }

    private void modelForStu(Model model, Student student, boolean oldStu, String link, String studentId) {
        model.addAttribute("student", student);
        model.addAttribute("oldStu", oldStu);
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("educations" , Student.Education.values());
        model.addAttribute("courses", courseService.findAllCourse());
        model.addAttribute("actionUrlForStu",link );
        model.addAttribute("studentId", studentId);
    }

    @GetMapping("/findAllStudent")
    @Admin
    public String findAllStudent(Model model) {
    	
         model.addAttribute("students", studentService.findAllStudent());
         return "student-list";
    }

    @GetMapping("/deleteStudent")
    @Admin
    public String deleteStudent(@RequestParam("id") int id, RedirectAttributes attributes) {

    	System.out.println("Id : "+id);
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
    	return "student-list";
    }

    boolean oldStu;
//    @ModelAttribute("oldStu")
    public boolean isOldStu(){
        return oldStu;
    }
}
