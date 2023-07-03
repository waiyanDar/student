package com.example.student.register.entity;

import java.time.LocalDate;

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
	
	private Gender gender;

	private Education education;

	
	public enum Gender {
		male,
		female
	}

	public enum Education{
		DIPLOMA("Diploma in IT"),
		BACHELOR("Bachelor of Computer Science");

		private final String displayName;

		Education(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

}
