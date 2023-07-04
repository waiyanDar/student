package com.example.student.register.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String studentId;
	private String name;

	@Column(name = "date_of_birth")
	private String  dateOfBirth;
	
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated( EnumType.STRING)
	private Education education;

	public enum Gender {
		MALE,
		FEMALE
	}

	public enum Education{
		DIPLOMA,
		BACHELOR
	}

	@PostPersist
	public void generateStudentId(){
		String formattedId = String.format("STU%03d",id);
		studentId = formattedId;
	}
}
