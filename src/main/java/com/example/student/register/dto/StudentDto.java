package com.example.student.register.dto;

import com.example.student.register.entity.Course;
import com.example.student.register.entity.Student;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
public class StudentDto {

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be empty")
    private String name;

    @Column(name = "date_of_birth")
    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be empty")
    private String dateOfBirth;

    @NotBlank(message = "Phone number cannot be blank")
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "[0-9]*", message = "Invalid phone number")
    private String phone;

    private MultipartFile photo;

    @NotNull(message = "Gender cannot be empty")
    private Student.Gender gender;

    @NotNull(message = "Education cannot be null")
    private Student.Education education;

    @NotNull(message = "course cannot be empty")
    private List<Course> courses;

    public static StudentDto form(Student student){
        StudentDto dto = new StudentDto();
        dto.setName(student.getName());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setPhone(student.getPhone());
        dto.setGender(student.getGender());
        dto.setEducation(student.getEducation());
        dto.setCourses(student.getCourses());
//        dto.setPhoto(student.getPhoto());

        return dto;
    }

}
