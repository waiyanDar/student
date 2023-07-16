package com.example.student.register.controller;

import com.example.student.register.dto.StudentDto;
import com.example.student.register.entity.Student;
import com.example.student.register.explorter.StudentExplorer;
import com.example.student.register.security.annotation.Admin;
import com.example.student.register.service.CourseService;
import com.example.student.register.service.StudentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;

import javax.validation.Valid;


@Controller
public class StudentController {

    private final StudentService studentService;
    
    private final CourseService courseService;

    private final StudentExplorer studentExplorer;
    
    public StudentController(StudentService studentService, CourseService courseService , StudentExplorer studentExplorer) {
    	this.studentService = studentService;
    	this.courseService = courseService;
    	this.studentExplorer = studentExplorer;
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

    	modelForStu(model,new StudentDto(), false, "/registerStudent", "");
    	return "student-form";
    }

    @PostMapping("/registerStudent")
    @Admin
    public String registerStudent(@Valid StudentDto studentDto, BindingResult result, Model model, RedirectAttributes attributes) throws IOException {
    	

        if (result.hasErrors()) {
            checkValidationForStu(result, model);
            modelForStu(model,studentDto,false, "", "");
            return "student-form";
        }

//        student.setPhoto(ArrayUtils.toObject(photo.getBytes()));
        Student student =  studentService.registerStudent(studentDto);
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
        StudentDto studentDto = StudentDto.form(oStudent);
        oId = oStudent.getId();
        oStudentId = oStudent.getStudentId();
        modelForStu(model, studentDto, true, "/updateStudent", oStudentId);
        return "student-form";
    }
    
    @PostMapping("/updateStudent")
    @Admin
    public String updateStudent(@Valid StudentDto studentDto, BindingResult result,RedirectAttributes attributes,Model model) {
        if (result.hasErrors()){
            checkValidationForStu(result, model);
            modelForStu(model, studentDto, true, "/updateStudent", oStudentId);
            return "student-form";
        }

       try {
           Student student = studentService.updateStudent(studentDto, oId, oStudentId);
           studentId = student.getStudentId();
           attributes.addFlashAttribute("stuUpdate", true);
       }catch (IOException e){
           modelForStu(model, studentDto, true, "/updateStudent", oStudentId);
       }
        return "redirect:/findAllStudent";
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

        studentId = studentService.deleteStudent(id);
        attributes.addFlashAttribute("stuDelete", true);
        return "redirect:/findAllStudent";
    }

    @GetMapping("/exportStudentToExcel")
    @Admin
    public String exportStudentToExcel(RedirectAttributes attributes) {
    	studentExplorer.explortStudentToExcel();
    	attributes.addFlashAttribute("exportExcel", true);
    	return "redirect:/findAllStudent";
    }

    boolean oldStu;
//    @ModelAttribute("oldStu")
    public boolean isOldStu(){
        return oldStu;
    }

    private <T> void modelForStu(Model model, T student, boolean oldStu, String link, String studentId) {
        model.addAttribute("student", student);
        model.addAttribute("oldStu", oldStu);
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("educations" , Student.Education.values());
        model.addAttribute("courses", courseService.findAllCourse());
        model.addAttribute("actionUrlForStu",link );
        model.addAttribute("studentId", studentId);
    }

    private void checkValidationForStu(BindingResult result, Model model) {
        if (result.hasFieldErrors("name")) {
            model.addAttribute("invalidName", result.getFieldError("name").getDefaultMessage());
        }
        if (result.hasFieldErrors("dateOfBirth")) {
            model.addAttribute("invalidDob", result.getFieldError("dateOfBirth").getDefaultMessage());
        }
        if (result.hasFieldErrors("phone")) {
            model.addAttribute("invalidPhone", result.getFieldError("phone").getDefaultMessage());
        }
        if (result.hasFieldErrors("gender")) {
            model.addAttribute("invalidGender", result.getFieldError("gender").getDefaultMessage());
        }
        if (result.hasFieldErrors("education")) {
            model.addAttribute("invalidEducation", result.getFieldError("education").getDefaultMessage());
        }
    }
}
