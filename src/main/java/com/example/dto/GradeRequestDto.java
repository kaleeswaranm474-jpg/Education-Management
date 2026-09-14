package com.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeRequestDto {

    @NotNull(message = "Enrollment ID is required")
    private Long enrollmentId;

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Marks obtained is required")
    @PositiveOrZero(message = "Marks cannot be negative")
    private Double marksObtained;

    private String grade;
}