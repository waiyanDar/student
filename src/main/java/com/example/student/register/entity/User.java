package com.example.student.register.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.student.register.dto.UserRegisterDto;
import com.example.student.register.dto.UserUpdateDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

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

//    @NotBlank(message = "Username cannot be blank")
//    @NotNull(message = "Username cannot be empty")
//    @Pattern(regexp = "[A-Z a-z]*", message = "Name cannot be illegal characters")
    private String username;

//    @Email(message = "Invalid Email format")
//    @NotBlank(message = "Email cannot be blank")
//    @NotNull(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

//    @NotNull(message = "password cannot be empty")
//    @NotBlank(message = "password cannot be empty")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @PostPersist
    public void generateUserId() {
        String formattedId = String.format("USR%03d", id);
        userId = formattedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }

    public static User formForRegisteration(UserRegisterDto userRegDto) {
        User user = new User();
        user.setUsername(userRegDto.getUsername());
        user.setEmail(userRegDto.getEmail());
        user.setPassword(userRegDto.getPassword());
//        user.setRoles(userRegDto.getRoles());
        return user;
    }
    
    public static User formForUpdate(UserUpdateDto updateDto) {
    	User user = new User();
    	user.setUsername(updateDto.getUsername());
    	user.setEmail(updateDto.getEmail());
//    	user.setRoles(updateDto.getRoles());
    	return user;
    }
    
    public void deleteRole() {
    	this.roles= null;
    }

}
