package com.example.student.register.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class UserAction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String userId;
	
//	@ElementCollection
//	private List<String> action = new ArrayList<String>();
	
	private String action;
	
	private String date;
}
