package com.example.student.register.dto;

import com.example.student.register.entity.Role;
import com.example.student.register.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.validation.constraints.*;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

    private String userId;
    @NotBlank(message = "Username cannot be blank")
    @NotNull(message = "Username cannot be empty")
    @Pattern(regexp = "[A-Z a-z]*", message = "Name cannot be illegal characters")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid Email format")
    @NotBlank(message = "Email cannot be blank")
    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Confirm password cannot be empty")
    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
    
    private MultipartFile photo;

    @NotEmpty(message = "Role cannot be empty")
    private List<Role> roles;

    public static UserRegisterDto form(User user) {

        UserRegisterDto userDto = new UserRegisterDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setConfirmPassword(user.getPassword());
        userDto.setRoles(user.getRoles());

        return userDto;
    }
}
