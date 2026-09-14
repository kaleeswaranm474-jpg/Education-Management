package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeResponseDto {

    private Long id;
    private Long enrollmentId;
    private Long examId;
    private String examName;
    private String studentName;
    private Double marksObtained;
    private String grade;
}