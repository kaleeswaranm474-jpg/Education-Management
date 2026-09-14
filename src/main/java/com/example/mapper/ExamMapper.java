package com.example.mapper;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.entity.Course;
import com.example.entity.Exam;
import org.springframework.stereotype.Component;

@Component
public class ExamMapper {

    public Exam toEntity(ExamRequestDto dto, Course course) {
        Exam exam = new Exam();

        exam.setExamName(dto.getExamName());
        exam.setCourse(course);
        exam.setExamDate(dto.getExamDate());
        exam.setTotalMarks(dto.getTotalMarks());

        return exam;
    }

    public ExamResponseDto toResponseDto(Exam exam) {

        Long courseId = null;
        String courseName = null;

        if (exam.getCourse() != null) {
            courseId = exam.getCourse().getId();
            courseName = exam.getCourse().getName();
        }

        return new ExamResponseDto(
                exam.getId(),
                exam.getExamName(),
                courseId,
                courseName,
                exam.getExamDate(),
                exam.getTotalMarks()
        );
    }

    public void updateEntity(
            Exam exam,
            ExamRequestDto dto,
            Course course) {

        exam.setExamName(dto.getExamName());
        exam.setCourse(course);
        exam.setExamDate(dto.getExamDate());
        exam.setTotalMarks(dto.getTotalMarks());
    }
}