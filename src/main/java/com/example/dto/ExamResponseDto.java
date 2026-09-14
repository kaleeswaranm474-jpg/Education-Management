package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponseDto {

    private Long id;
    private String examName;
    private Long courseId;
    private String courseName;
    private LocalDate examDate;
    private Integer totalMarks;
}