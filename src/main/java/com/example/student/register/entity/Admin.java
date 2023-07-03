package com.example.student.register.entity;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String adminId;

	@NotBlank(message = "Username cannot be blank")
	@NotNull(message = "Username cannot be empty")
	@Pattern(regexp = "[A-Z a-z]*", message = "Name cannot be illegal characters")
	@Column(unique = true)
	private String username;

	@Email(message = "Invalid Email format")
	private String email;

	@NotNull(message = "password cannot be empty")
	@NotBlank(message = "password cannot be empty")
	private String password;
	
	@ManyToOne
	private Role role;
	
}
