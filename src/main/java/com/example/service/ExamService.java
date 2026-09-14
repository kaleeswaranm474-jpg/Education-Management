package com.example.service;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.entity.Course;
import com.example.entity.Exam;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ExamMapper;
import com.example.repository.CourseRepository;
import com.example.repository.ExamRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamService {

    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamMapper examMapper;


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    public ExamResponseDto createExam(
            ExamRequestDto dto) {

        Course course =
                courseRepository
                        .findById(dto.getCourseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id: "
                                                + dto.getCourseId()
                                )
                        );

        Exam exam =
                examMapper.toEntity(
                        dto,
                        course
                );

        Exam savedExam =
                examRepository.save(exam);

        return examMapper.toResponseDto(
                savedExam
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public Page<ExamResponseDto> getAllExams(
            Pageable pageable) {

        return examRepository
                .findAll(pageable)
                .map(examMapper::toResponseDto);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public ExamResponseDto getExamById(
            Long id) {

        Exam exam =
                examRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam not found with id: "
                                                + id
                                )
                        );

        return examMapper.toResponseDto(exam);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    public ExamResponseDto updateExam(
            Long id,
            ExamRequestDto dto) {

        Exam exam =
                examRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam not found with id: "
                                                + id
                                )
                        );

        Course course =
                courseRepository
                        .findById(dto.getCourseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Course not found with id: "
                                                + dto.getCourseId()
                                )
                        );

        examMapper.updateEntity(
                exam,
                dto,
                course
        );

        Exam updatedExam =
                examRepository.save(exam);

        return examMapper.toResponseDto(
                updatedExam
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    public void deleteExam(Long id) {

        Exam exam =
                examRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam not found with id: "
                                                + id
                                )
                        );

        examRepository.delete(exam);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN', 'TEACHER')"
    )
    @Transactional(readOnly = true)
    public Page<ExamResponseDto> searchExams(
            String examName,
            Long courseId,
            Pageable pageable) {

        boolean hasExamName =
                examName != null
                        && !examName.trim().isEmpty();

        boolean hasCourseId =
                courseId != null;


        if (hasExamName && hasCourseId) {

            return examRepository
                    .findByExamNameContainingIgnoreCaseAndCourseId(
                            examName.trim(),
                            courseId,
                            pageable
                    )
                    .map(examMapper::toResponseDto);
        }


        if (hasExamName) {

            return examRepository
                    .findByExamNameContainingIgnoreCase(
                            examName.trim(),
                            pageable
                    )
                    .map(examMapper::toResponseDto);
        }


        if (hasCourseId) {

            return examRepository
                    .findByCourseId(
                            courseId,
                            pageable
                    )
                    .map(examMapper::toResponseDto);
        }


        return examRepository
                .findAll(pageable)
                .map(examMapper::toResponseDto);
    }
}