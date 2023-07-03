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

    public void register(Student student){
        int nextId= studentDao.findAll().size()+1;
        String formattedId= String.format("STU%03d",nextId);
        student.setStudentId(formattedId);
        studentDao.save(student);
    }

    public Student findStudent(int id){
        return studentDao.findById(id).get();
    }

    public List<Student> findAll() {
        return studentDao.findAll();
    }

    public void deleteStudent(int id) {
        Student student = findStudent(id);
        studentDao.delete(student);
    }
}
