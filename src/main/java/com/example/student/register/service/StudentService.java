package com.example.student.register.service;

import static com.example.student.register.util.SpecificationUtil.*;

import com.example.student.register.dao.StudentDao;
import com.example.student.register.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    private StudentDao studentDao;
    
    public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

    public void registerStudent(Student student) {
        studentDao.save(student);
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

    public Student updateStudent(Student student) {
        Student oStudent = findStudent(student.getId());
        oStudent.setName(student.getName());
        oStudent.setPhone(student.getPhone());
        oStudent.setDateOfBirth(student.getDateOfBirth());
        oStudent.setCourses(student.getCourses());
        oStudent.setGender(student.getGender());
        oStudent.setEducation(student.getEducation());
        return studentDao.saveAndFlush(oStudent);
    }
    
    public List<Student> searchStudent(Optional<String> studentId,Optional<String> name, Optional<String> courseName){
    	Specification<Student> specification = withStudentId(studentId).and(withStudentName(name)).and(withCourse(courseName));
    	return studentDao.findAll(specification);
    }
}
