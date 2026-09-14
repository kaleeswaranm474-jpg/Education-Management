package com.example.mapper;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.entity.Student;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    // Convert DTO to Entity
    public Student toEntity(StudentRequestDto dto) {

        Student student = new Student();

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setDepartment(dto.getDepartment());

        return student;
    }

    // Convert Entity to Response DTO
    public StudentResponseDto toResponseDto(Student student) {

        return new StudentResponseDto(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getDepartment()
        );
    }

    // Update existing Entity using DTO
    public void updateEntity(
            Student student,
            StudentRequestDto dto) {

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setDepartment(dto.getDepartment());
    }
}