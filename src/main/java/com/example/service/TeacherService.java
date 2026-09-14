package com.example.service;

import com.example.dto.TeacherRequestDto;
import com.example.dto.TeacherResponseDto;
import com.example.entity.Teacher;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.TeacherMapper;
import com.example.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;


    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponseDto createTeacher(
            TeacherRequestDto dto) {

        if (teacherRepository.existsByEmail(
                dto.getEmail())) {

            throw new BadRequestException(
                    "Teacher email already exists"
            );
        }

        Teacher teacher =
                teacherMapper.toEntity(dto);

        Teacher savedTeacher =
                teacherRepository.save(teacher);

        return teacherMapper.toResponseDto(
                savedTeacher
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public Page<TeacherResponseDto> getAllTeachers(
            Pageable pageable) {

        return teacherRepository
                .findAll(pageable)
                .map(teacherMapper::toResponseDto);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public TeacherResponseDto getTeacherById(
            Long id) {

        Teacher teacher =
                teacherRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher not found with id: "
                                                + id
                                )
                        );

        return teacherMapper.toResponseDto(teacher);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponseDto updateTeacher(
            Long id,
            TeacherRequestDto dto) {

        Teacher teacher =
                teacherRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher not found with id: "
                                                + id
                                )
                        );

        if (!teacher.getEmail().equals(dto.getEmail())
                && teacherRepository.existsByEmail(
                        dto.getEmail())) {

            throw new BadRequestException(
                    "Teacher email already exists"
            );
        }

        teacherMapper.updateEntity(
                teacher,
                dto
        );

        Teacher updatedTeacher =
                teacherRepository.save(teacher);

        return teacherMapper.toResponseDto(
                updatedTeacher
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTeacher(Long id) {

        Teacher teacher =
                teacherRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher not found with id: "
                                                + id
                                )
                        );

        teacherRepository.delete(teacher);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public Page<TeacherResponseDto> searchTeachers(
            String name,
            String department,
            Pageable pageable) {

        boolean hasName =
                name != null
                        && !name.trim().isEmpty();

        boolean hasDepartment =
                department != null
                        && !department.trim().isEmpty();


        if (hasName && hasDepartment) {

            return teacherRepository
                    .findByNameContainingIgnoreCaseAndDepartmentIgnoreCase(
                            name.trim(),
                            department.trim(),
                            pageable
                    )
                    .map(teacherMapper::toResponseDto);
        }


        if (hasName) {

            return teacherRepository
                    .findByNameContainingIgnoreCase(
                            name.trim(),
                            pageable
                    )
                    .map(teacherMapper::toResponseDto);
        }


        if (hasDepartment) {

            return teacherRepository
                    .findByDepartmentIgnoreCase(
                            department.trim(),
                            pageable
                    )
                    .map(teacherMapper::toResponseDto);
        }


        return teacherRepository
                .findAll(pageable)
                .map(teacherMapper::toResponseDto);
    }
}