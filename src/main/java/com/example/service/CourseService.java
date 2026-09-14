package com.example.service;

import com.example.dto.CourseRequestDto;
import com.example.dto.CourseResponseDto;
import com.example.entity.Course;
import com.example.entity.Teacher;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CourseMapper;
import com.example.repository.CourseRepository;
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
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    public CourseResponseDto createCourse(
            CourseRequestDto dto) {

        if (courseRepository.existsByCourseCode(
                dto.getCourseCode())) {

            throw new BadRequestException(
                    "Course code already exists: "
                            + dto.getCourseCode()
            );
        }

        Teacher teacher =
                teacherRepository
                        .findById(dto.getTeacherId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher not found with id: "
                                                + dto.getTeacherId()
                                )
                        );

        Course course =
                courseMapper.toEntity(
                        dto,
                        teacher
                );

        Course savedCourse =
                courseRepository.save(course);

        return courseMapper.toResponseDto(
                savedCourse
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public Page<CourseResponseDto> getAllCourses(
            Pageable pageable) {

        return courseRepository
                .findAll(pageable)
                .map(courseMapper::toResponseDto);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public CourseResponseDto getCourseById(
            Long id) {

        Course course =
                courseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id: "
                                                + id
                                )
                        );

        return courseMapper.toResponseDto(course);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    public CourseResponseDto updateCourse(
            Long id,
            CourseRequestDto dto) {

        Course course =
                courseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id: "
                                                + id
                                )
                        );

        if (!course.getCourseCode().equals(
                dto.getCourseCode())
                && courseRepository.existsByCourseCode(
                        dto.getCourseCode())) {

            throw new BadRequestException(
                    "Course code already exists: "
                            + dto.getCourseCode()
            );
        }

        Teacher teacher =
                teacherRepository
                        .findById(dto.getTeacherId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Teacher not found with id: "
                                                + dto.getTeacherId()
                                )
                        );

        courseMapper.updateEntity(
                course,
                dto,
                teacher
        );

        Course updatedCourse =
                courseRepository.save(course);

        return courseMapper.toResponseDto(
                updatedCourse
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    public void deleteCourse(Long id) {

        Course course =
                courseRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id: "
                                                + id
                                )
                        );

        courseRepository.delete(course);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public Page<CourseResponseDto> searchCourses(
            String name,
            Long teacherId,
            Pageable pageable) {

        boolean hasName =
                name != null
                        && !name.trim().isEmpty();

        boolean hasTeacherId =
                teacherId != null;


        if (hasName && hasTeacherId) {

            return courseRepository
                    .findByNameContainingIgnoreCaseAndTeacherId(
                            name.trim(),
                            teacherId,
                            pageable
                    )
                    .map(courseMapper::toResponseDto);
        }


        if (hasName) {

            return courseRepository
                    .findByNameContainingIgnoreCase(
                            name.trim(),
                            pageable
                    )
                    .map(courseMapper::toResponseDto);
        }


        if (hasTeacherId) {

            return courseRepository
                    .findByTeacherId(
                            teacherId,
                            pageable
                    )
                    .map(courseMapper::toResponseDto);
        }


        return courseRepository
                .findAll(pageable)
                .map(courseMapper::toResponseDto);
    }
}