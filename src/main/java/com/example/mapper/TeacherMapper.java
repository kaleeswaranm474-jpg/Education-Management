package com.example.mapper;

import com.example.dto.TeacherRequestDto;
import com.example.dto.TeacherResponseDto;
import com.example.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherRequestDto dto) {
        Teacher teacher = new Teacher();

        teacher.setName(dto.getName());
        teacher.setEmail(dto.getEmail());
        teacher.setPhone(dto.getPhone());
        teacher.setDepartment(dto.getDepartment());

        return teacher;
    }

    public TeacherResponseDto toResponseDto(Teacher teacher) {
        return new TeacherResponseDto(
                teacher.getId(),
                teacher.getName(),
                teacher.getEmail(),
                teacher.getPhone(),
                teacher.getDepartment()
        );
    }

    public void updateEntity(Teacher teacher, TeacherRequestDto dto) {
        teacher.setName(dto.getName());
        teacher.setEmail(dto.getEmail());
        teacher.setPhone(dto.getPhone());
        teacher.setDepartment(dto.getDepartment());
    }
}