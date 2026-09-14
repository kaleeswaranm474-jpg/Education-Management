package com.example.service;

import com.example.dto.EnrollmentRequestDto;
import com.example.dto.EnrollmentResponseDto;
import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Student;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.EnrollmentMapper;
import com.example.repository.CourseRepository;
import com.example.repository.EnrollmentRepository;
import com.example.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentResponseDto createEnrollment(
            EnrollmentRequestDto dto) {

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                dto.getStudentId(),
                dto.getCourseId())) {

            throw new BadRequestException(
                    "Student is already enrolled in this course");
        }

        Student student =
                studentRepository
                        .findById(dto.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: "
                                                + dto.getStudentId()));

        Course course =
                courseRepository
                        .findById(dto.getCourseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id: "
                                                + dto.getCourseId()));

        Enrollment enrollment =
                enrollmentMapper.toEntity(
                        dto,
                        student,
                        course);

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return enrollmentMapper
                .toResponseDto(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentResponseDto> getAllEnrollments(
            Pageable pageable) {

        return enrollmentRepository.findAll(pageable)
                .map(enrollmentMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public EnrollmentResponseDto getEnrollmentById(
            Long id) {

        Enrollment enrollment =
                enrollmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Enrollment not found with id: "
                                                + id));

        return enrollmentMapper
                .toResponseDto(enrollment);
    }

    public EnrollmentResponseDto updateEnrollment(
            Long id,
            EnrollmentRequestDto dto) {

        Enrollment enrollment =
                enrollmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Enrollment not found with id: "
                                                + id));

        boolean duplicate =
                enrollmentRepository.existsByStudentIdAndCourseId(
                        dto.getStudentId(),
                        dto.getCourseId());

        if (duplicate) {

            boolean sameAsCurrent =
                    enrollment.getStudent() != null
                            && enrollment.getCourse() != null
                            && enrollment.getStudent().getId()
                                    .equals(dto.getStudentId())
                            && enrollment.getCourse().getId()
                                    .equals(dto.getCourseId());

            if (!sameAsCurrent) {

                throw new BadRequestException(
                        "Student is already enrolled in this course");
            }
        }

        Student student =
                studentRepository
                        .findById(dto.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Student not found with id: "
                                                + dto.getStudentId()));

        Course course =
                courseRepository
                        .findById(dto.getCourseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id: "
                                                + dto.getCourseId()));

        enrollmentMapper.updateEntity(
                enrollment,
                student,
                course);

        Enrollment updatedEnrollment =
                enrollmentRepository.save(enrollment);

        return enrollmentMapper
                .toResponseDto(updatedEnrollment);
    }

    public void deleteEnrollment(Long id) {

        Enrollment enrollment =
                enrollmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Enrollment not found with id: "
                                                + id));

        enrollmentRepository.delete(enrollment);
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentResponseDto> searchEnrollments(
            Long studentId,
            Long courseId,
            Pageable pageable) {

        boolean hasStudentId =
                studentId != null;

        boolean hasCourseId =
                courseId != null;

        if (hasStudentId && hasCourseId) {

            return enrollmentRepository
                    .findByStudentIdAndCourseId(
                            studentId,
                            courseId,
                            pageable)
                    .map(enrollmentMapper::toResponseDto);
        }

        if (hasStudentId) {

            return enrollmentRepository
                    .findByStudentId(
                            studentId,
                            pageable)
                    .map(enrollmentMapper::toResponseDto);
        }

        if (hasCourseId) {

            return enrollmentRepository
                    .findByCourseId(
                            courseId,
                            pageable)
                    .map(enrollmentMapper::toResponseDto);
        }

        return enrollmentRepository.findAll(pageable)
                .map(enrollmentMapper::toResponseDto);
    }
}