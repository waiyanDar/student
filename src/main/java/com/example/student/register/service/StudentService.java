package com.example.student.register.service;


import com.example.student.register.dao.StudentDao;
import com.example.student.register.dto.StudentDto;
import com.example.student.register.entity.Student;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StudentService {
    
    private StudentDao studentDao;
    
    public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

//    public void registerStudent(Student student) {
//        studentDao.save(student);
//    }

    public Student registerStudent(StudentDto studentDto) throws IOException {
        Student student = Student.form(studentDto);
        return studentDao.save(student);
    }
    public Student findStudent(int id) {
        return studentDao.findById(id).get();
    }

    public List<Student> findAllStudent() {
        return studentDao.findAll();
    }

    public String  deleteStudent(int id) {
        Student student = findStudent(id);
        String studentId = student.getStudentId();
        studentDao.delete(student);
        return studentId;
    }

    public Student updateStudent(StudentDto student, int id, String oStudentId) throws IOException {
        Student oStudent = findStudent(id);
        oStudent.setId(id);
        oStudent.setStudentId(oStudentId);
        oStudent.setName(student.getName());
        oStudent.setPhone(student.getPhone());
        oStudent.setPhoto(null);
        oStudent.setPhoto(ArrayUtils.toObject(student.getPhoto().getBytes()));
        oStudent.setDateOfBirth(student.getDateOfBirth());
        oStudent.setCourses(student.getCourses());
        oStudent.setGender(student.getGender());
        oStudent.setEducation(student.getEducation());
        return studentDao.saveAndFlush(oStudent);
    }

}
