package com.example.student.register.controller;

import com.example.student.register.entity.Student;
import com.example.student.register.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/registerStu")
    public String registerStu(@RequestBody Student student, BindingResult result){
        if (result.hasErrors()){
            return "something's wrong";
        }

        studentService.register(student);
        return "successfully register";
    }

    @GetMapping("/getStudent")
    public String getStudent(@RequestParam("id") int id){
       return studentService.findStudent(id).toString();
    }

    @GetMapping("/findAll")
    public List<Student> findAllStudent(){
        return studentService.findAll();
    }
    @DeleteMapping("/delete")
    public String deleteStudent(@RequestParam("id") int id){
        studentService.deleteStudent(id);
        return "successfully delete";
    }
    
    public ResponseEntity<?> updateStudent(@RequestBody Student student){
    	
    	return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(student));
    }

}
