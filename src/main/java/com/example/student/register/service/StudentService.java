package com.example.student.register.service;

import com.example.student.register.dao.StudentDao;
import com.example.student.register.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentDao studentDao;

    public void registerStudent(Student student){
        studentDao.save(student);
    }

    public Student findStudent(int id){
        return studentDao.findById(id).get();
    }

    public List<Student> findAllStudent() {
        return studentDao.findAll();
    }

    public void deleteStudent(int id) {
        Student student = findStudent(id);
        studentDao.delete(student);
    }
    
    public Student updateStudent(Student student) {
    	Student oStudent = findStudent(student.getId());
    	oStudent.setName(student.getName());
    	oStudent.setDateOfBirth(student.getDateOfBirth());
    	oStudent.setPhone(student.getPhone());
    	oStudent.setGender(student.getGender());
    	oStudent.setEducation(student.getEducation());
    	return studentDao.saveAndFlush(oStudent);
    }
}
