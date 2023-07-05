package com.example.student.register.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String courseId;
    @NotNull(message = "Course name cannot be null")
    @NotBlank(message = "Course name cannot be blank")
    private String name;

    @PostPersist
    public void generateCourseId() {
        String formattedId = String.format("COU%03d", id);
        courseId = formattedId;
    }

}
