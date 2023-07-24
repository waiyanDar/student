package com.example.student.register.dto;

import java.util.List;

import com.example.student.register.entity.Student;

import lombok.Data;

@Data
public class StudentPaginationDto {
	
	private int draw;
	private int recordTotal;
	private int recordsFiltered;
	
	private List<Student> data;

}
