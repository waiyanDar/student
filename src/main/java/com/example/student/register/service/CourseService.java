package com.example.student.register.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.CourseDao;
import com.example.student.register.dto.CourseDto;
import com.example.student.register.entity.Course;

@Service
public class CourseService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private SmbService smbService;

    public void addCourse(CourseDto courseDto) {

        Course course = new Course();
        course.setName(courseDto.getName());

        String photoPath = smbService.storePhoto(courseDto.getName(), courseDto.getPhoto());
        course.setPhotoPath(photoPath);

        courseDao.save(course);
    }

    public List<Course> findAllCourse() {
        return courseDao.findAll();
    }

}
