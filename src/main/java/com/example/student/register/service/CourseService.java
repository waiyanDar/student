package com.example.student.register.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.register.dao.CourseDao;
import com.example.student.register.dto.CourseDto;
import com.example.student.register.entity.Course;
import com.hierynomus.smbj.io.InputStreamByteChunkProvider;

@Service
public class CourseService {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private SmbService smbService;

	public void addCourse(CourseDto courseDto) {
		
		Course course = new Course();
		course.setName(courseDto.getName());
	
		File file = new File(courseDto.getPhoto().getOriginalFilename());
		
//		try {
//			FileOutputStream fileOutputStream = new FileOutputStream(file);
//			fileOutputStream.write(courseDto.getPhoto().getBytes());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		String photoPath = smbService.storePhoto(courseDto.getPhoto());
		course.setPhotoPath(photoPath);
		
		courseDao.save(course);
	}

	public List<Course> findAllCourse() {
		return courseDao.findAll();
	}

}
