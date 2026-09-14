package com.example.mapper;

import com.example.dto.EnrollmentRequestDto;
import com.example.dto.EnrollmentResponseDto;
import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public Enrollment toEntity(
            EnrollmentRequestDto dto,
            Student student,
            Course course) {

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollment;
    }

    public EnrollmentResponseDto toResponseDto(Enrollment enrollment) {

        Long studentId = null;
        String studentName = null;
        Long courseId = null;
        String courseName = null;

        if (enrollment.getStudent() != null) {
            studentId = enrollment.getStudent().getId();
            studentName = enrollment.getStudent().getName();
        }

        if (enrollment.getCourse() != null) {
            courseId = enrollment.getCourse().getId();
            courseName = enrollment.getCourse().getName();
        }

        return new EnrollmentResponseDto(
                enrollment.getId(),
                studentId,
                studentName,
                courseId,
                courseName,
                enrollment.getEnrollmentDate()
        );
    }

    public void updateEntity(
            Enrollment enrollment,
            Student student,
            Course course) {

        enrollment.setStudent(student);
        enrollment.setCourse(course);
    }
}