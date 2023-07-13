package com.example.student.register.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class InvalidJwt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
	
	private String token;
	
	public InvalidJwt() {
		
	}
	
	public InvalidJwt(String token) {
		this.token = token;
	}

}
