package com.example.service;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.entity.Student;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.StudentMapper;
import com.example.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;


    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public StudentResponseDto createStudent(
            StudentRequestDto dto) {

        if (studentRepository.existsByEmail(dto.getEmail())) {

            throw new BadRequestException(
                    "Student email already exists"
            );
        }

        Student student =
                studentMapper.toEntity(dto);

        Student savedStudent =
                studentRepository.save(student);

        return studentMapper.toResponseDto(savedStudent);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')"
    )
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> getAllStudents(
            Pageable pageable) {

        return studentRepository
                .findAll(pageable)
                .map(studentMapper::toResponseDto);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')"
    )
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(
            Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: "
                                                + id
                                )
                        );

        return studentMapper.toResponseDto(student);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public StudentResponseDto updateStudent(
            Long id,
            StudentRequestDto dto) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: "
                                                + id
                                )
                        );

        if (!student.getEmail().equals(dto.getEmail())
                && studentRepository.existsByEmail(
                        dto.getEmail())) {

            throw new BadRequestException(
                    "Student email already exists"
            );
        }

        studentMapper.updateEntity(
                student,
                dto
        );

        Student updatedStudent =
                studentRepository.save(student);

        return studentMapper.toResponseDto(
                updatedStudent
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: "
                                                + id
                                )
                        );

        studentRepository.delete(student);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')"
    )
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> searchStudents(
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

            return studentRepository
                    .findByNameContainingIgnoreCaseAndDepartmentIgnoreCase(
                            name.trim(),
                            department.trim(),
                            pageable
                    )
                    .map(studentMapper::toResponseDto);
        }


        if (hasName) {

            return studentRepository
                    .findByNameContainingIgnoreCase(
                            name.trim(),
                            pageable
                    )
                    .map(studentMapper::toResponseDto);
        }


        if (hasDepartment) {

            return studentRepository
                    .findByDepartmentIgnoreCase(
                            department.trim(),
                            pageable
                    )
                    .map(studentMapper::toResponseDto);
        }


        return studentRepository
                .findAll(pageable)
                .map(studentMapper::toResponseDto);
    }
}