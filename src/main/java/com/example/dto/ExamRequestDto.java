package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamRequestDto {

    @NotBlank(message = "Exam name is required")
    private String examName;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Exam date is required")
    private LocalDate examDate;

    @NotNull(message = "Total marks is required")
    private Integer totalMarks;
}