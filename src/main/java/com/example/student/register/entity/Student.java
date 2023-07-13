package com.example.student.register.entity;

import java.io.IOException;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.student.register.dto.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String studentId;
    
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

    @Column(name = "photo", columnDefinition = "BLOB")
    private Byte[] photo;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender cannot be empty")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Education cannot be null")
    private Education education;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
    	    name = "student_course",
    	    joinColumns = @JoinColumn(name = "student_id"),
    	    inverseJoinColumns = @JoinColumn(name = "course_id")
    	)
    @NotNull(message = "course cannot be empty")
    private List<Course> courses;

    public enum Gender {
        MALE,
        FEMALE
    }

    public enum Education {
        DIPLOMA,
        BACHELOR
    }

    @PostPersist
    public void generateStudentId() {
      studentId = String.format("STU%03d", id);
    }

    public static Student form (StudentDto studentDto) throws IOException {
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setDateOfBirth(studentDto.getDateOfBirth());
        student.setPhone(studentDto.getPhone());
        student.setPhoto(ArrayUtils.toObject(studentDto.getPhoto().getBytes()));
        student.setGender(studentDto.getGender());
        student.setEducation(studentDto.getEducation());
        student.setCourses(studentDto.getCourses());

        return student;
    }
}
