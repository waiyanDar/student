package com.example.student.register.service;


import com.example.student.register.dao.SpecificationUtil;
import com.example.student.register.dao.StudentDao;
import com.example.student.register.dto.StudentDto;
import com.example.student.register.entity.Student;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StudentService {

    private StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public Student registerStudent(StudentDto studentDto) throws IOException {
        Student student = Student.form(studentDto);
        return studentDao.save(student);
    }

    public Student findStudent(int id) {
        return studentDao.findById(id).get();
    }

    public Student findStudentByStudentId(String studentId) {
        return studentDao.findStudentByStudentId(studentId).get();
    }

    public List<Student> findAllStudent() {
        return studentDao.findAll();
    }

    public String deleteStudent(String studentId) {
        Student student = findStudentByStudentId(studentId);
        studentDao.delete(student);
        return studentId;
    }

    public Student updateStudent(StudentDto student, int id, String oStudentId) throws IOException {
        Student oStudent = findStudent(id);
        oStudent.setId(id);
        oStudent.setStudentId(oStudentId);
        oStudent.setName(student.getName());
        oStudent.setPhone(student.getPhone());
        
        if(!student.getPhoto().isEmpty()) {
        	oStudent.setPhoto(null);
            oStudent.setPhoto(student.getPhoto().getBytes());
        }
        
        oStudent.setDateOfBirth(student.getDateOfBirth());
        oStudent.setCourses(student.getCourses());
        oStudent.setGender(student.getGender());
        oStudent.setEducation(student.getEducation());
        return studentDao.saveAndFlush(oStudent);
    }

    public List<Student> searchStudentWithAsc(int page, int size, String searchTerm, String column) {
        List<Student> listStudent = studentDao
                .findAll(SpecificationUtil.studentWithSearchTerm(searchTerm),
                        PageRequest.of(page, size, Sort.by(column).ascending())).getContent();
        return listStudent;
    }

    public List<Student> searchStudentWithDesc(int page, int size, String searchTerm, String column) {
        List<Student> listStudent = studentDao
                .findAll(SpecificationUtil.studentWithSearchTerm(searchTerm),
                        PageRequest.of(page, size, Sort.by(column).descending())).getContent();
        return listStudent;
    }

}
