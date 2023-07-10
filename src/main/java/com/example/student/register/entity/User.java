package com.example.student.register.entity;

import javax.persistence.*;

import com.example.student.register.dto.UserRegisterDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @PostPersist
    public void generateUserId() {
        userId  = String.format("USR%03d", id);
    }

    public static User formForRegistration(UserRegisterDto userRegDto) {
        User user = new User();
        user.setUsername(userRegDto.getUsername());
        user.setEmail(userRegDto.getEmail());
        user.setPassword(userRegDto.getPassword());
        return user;
    }

    public void deleteRole() {
    	this.roles= null;
    }

}
