package com.example.student.register.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CourseDto {
	
	@NotBlank(message = "Course name cannot be blank")
	@NotNull (message = "Course name must have")
	private String name;
	
	private MultipartFile photo;
}
