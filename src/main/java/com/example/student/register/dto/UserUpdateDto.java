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
public class UserUpdateDto {

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

    private MultipartFile photo;
    
    @NotEmpty(message = "Role cannot be empty")
    private List<Role> roles;
    
    private String button;

    public static UserUpdateDto form(User user) {

        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
//        user.deletePhoto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        userDto.setButton("<a th:href=\"@{'/userUpdate?userId='+${user.getUserId}}\"\r\n" + 
        		"									class=\"btn btn-success me-3\" type=\"button\">Update</a>\r\n" + 
        		"\r\n" + 
        		"									<button data-bs-toggle=\"modal\"\r\n" + 
        		"										th:data-bs-target=\"'#exampleModal'+${user.id}\" type=\"button\"\r\n" + 
        		"										class=\"btn btn-danger\">Delete</button>\r\n" + 
        		"\r\n" + 
        		"									<div class=\"modal\" th:id=\"'exampleModal'+${user.id}\"\r\n" + 
        		"										tabindex=\"-1\" aria-labelledby=\"exampleModalLabel\"\r\n" + 
        		"										aria-hidden=\"true\">\r\n" + 
        		"										<div class=\"modal-dialog modal-dialog-centered\">\r\n" + 
        		"											<div class=\"modal-content\">\r\n" + 
        		"												<div class=\"modal-header\">\r\n" + 
        		"													<h5 class=\"modal-title text-dark\" id=\"exampleModalLabel\">Confirmation</h5>\r\n" + 
        		"													<button type=\"button\" class=\"btn-close\"\r\n" + 
        		"														data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\r\n" + 
        		"												</div>\r\n" + 
        		"												<div class=\"modal-body\">\r\n" + 
        		"													<h5 class=\"text-danger\">\r\n" + 
        		"														Are you sure to delete <span th:text=\"${user.username}\"></span>\r\n" + 
        		"														?\r\n" + 
        		"													</h5>\r\n" + 
        		"												</div>\r\n" + 
        		"												<div class=\"modal-footer\">\r\n" + 
        		"													<a th:href=\"@{'/deleteUser?id='+${user.id}}\">\r\n" + 
        		"														<button type=\"submit\" class=\"btn btn-danger \"\r\n" + 
        		"															data-bs-dismiss=\"modal\">Yes</button>\r\n" + 
        		"													</a>\r\n" + 
        		"													<button type=\"button\" class=\"btn btn-warning \"\r\n" + 
        		"														data-bs-dismiss=\"modal\">No</button>\r\n" + 
        		"												</div>\r\n" + 
        		"											</div>\r\n" + 
        		"										</div>\r\n" + 
        		"									</div>");
        return userDto;
    }
}
