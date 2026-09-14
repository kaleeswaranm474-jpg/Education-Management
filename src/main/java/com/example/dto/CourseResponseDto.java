package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

    private Long id;
    private String name;
    private String courseCode;
    private String description;
    private Long teacherId;
    private String teacherName;
}