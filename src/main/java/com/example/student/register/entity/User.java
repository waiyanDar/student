package com.example.student.register.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.student.register.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String userId;

	@NotBlank(message = "Username cannot be blank")
	@NotNull(message = "Username cannot be empty")
	@Pattern(regexp = "[A-Z a-z]*", message = "Name cannot be illegal characters")
	private String username;

	@Email(message = "Invalid Email format")
	@NotBlank(message = "Email cannot be blank")
	@NotEmpty(message = "Email cannot be empty")
	private String email;

	@NotNull(message = "password cannot be empty")
	@NotBlank(message = "password cannot be empty")
	private String password;

	@ManyToOne
	private Role role;

	@PostPersist
	public void generateUserId(){
		String formattedId = String.format("USR%03d",id);
		userId = formattedId;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public static User form(UserDto userDto){
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		return user;
	}
	
}
