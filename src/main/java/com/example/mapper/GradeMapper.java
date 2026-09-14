package com.example.mapper;

import com.example.dto.GradeRequestDto;
import com.example.dto.GradeResponseDto;
import com.example.entity.Enrollment;
import com.example.entity.Exam;
import com.example.entity.Grade;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {

    public Grade toEntity(
            GradeRequestDto dto,
            Enrollment enrollment,
            Exam exam) {

        Grade grade = new Grade();

        grade.setEnrollment(enrollment);
        grade.setExam(exam);
        grade.setMarksObtained(dto.getMarksObtained());
        grade.setGrade(dto.getGrade());

        return grade;
    }

    public GradeResponseDto toResponseDto(Grade grade) {

        Long enrollmentId = null;
        Long examId = null;
        String examName = null;
        String studentName = null;

        if (grade.getEnrollment() != null) {
            enrollmentId = grade.getEnrollment().getId();

            if (grade.getEnrollment().getStudent() != null) {
                studentName = grade.getEnrollment()
                        .getStudent()
                        .getName();
            }
        }

        if (grade.getExam() != null) {
            examId = grade.getExam().getId();
            examName = grade.getExam().getExamName();
        }

        return new GradeResponseDto(
                grade.getId(),
                enrollmentId,
                examId,
                examName,
                studentName,
                grade.getMarksObtained(),
                grade.getGrade()
        );
    }

    public void updateEntity(
            Grade grade,
            GradeRequestDto dto,
            Enrollment enrollment,
            Exam exam) {

        grade.setEnrollment(enrollment);
        grade.setExam(exam);
        grade.setMarksObtained(dto.getMarksObtained());
        grade.setGrade(dto.getGrade());
    }
}