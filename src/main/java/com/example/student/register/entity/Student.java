package com.example.student.register.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Student {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "student_id_generator")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	private LocalDate dateOfBirth;
	
	private String phone;
	
	@Enumerated
	private Gender gender;
	
	public enum Gender {
		
		male,
		female
	}

}
