package com.example.service;

import com.example.dto.GradeRequestDto;
import com.example.dto.GradeResponseDto;
import com.example.entity.Enrollment;
import com.example.entity.Exam;
import com.example.entity.Grade;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.GradeMapper;
import com.example.repository.EnrollmentRepository;
import com.example.repository.ExamRepository;
import com.example.repository.GradeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;

    private final EnrollmentRepository enrollmentRepository;

    private final ExamRepository examRepository;

    private final GradeMapper gradeMapper;


    public GradeResponseDto createGrade(
            GradeRequestDto dto) {

        Enrollment enrollment =
                enrollmentRepository
                        .findById(dto.getEnrollmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Enrollment not found with id: "
                                                + dto.getEnrollmentId()
                                )
                        );

        Exam exam =
                examRepository
                        .findById(dto.getExamId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam not found with id: "
                                                + dto.getExamId()
                                )
                        );


        if (gradeRepository
                .existsByEnrollmentIdAndExamId(
                        dto.getEnrollmentId(),
                        dto.getExamId())) {

            throw new BadRequestException(
                    "Invalid Data: Grade already exists for this enrollment and exam"
            );
        }


        if (enrollment.getCourse() == null
                || exam.getCourse() == null
                || !enrollment.getCourse().getId()
                        .equals(exam.getCourse().getId())) {

            throw new BadRequestException(
                    "Exam course does not match enrollment course"
            );
        }


        if (dto.getMarksObtained() > exam.getTotalMarks()) {

            throw new BadRequestException(
                    "Marks obtained cannot be greater than total marks"
            );
        }


        Grade grade =
                gradeMapper.toEntity(
                        dto,
                        enrollment,
                        exam
                );

        Grade savedGrade =
                gradeRepository.save(grade);

        return gradeMapper.toResponseDto(
                savedGrade
        );
    }


    @Transactional(readOnly = true)
    public Page<GradeResponseDto> getAllGrades(
            Pageable pageable) {

        return gradeRepository
                .findAll(pageable)
                .map(gradeMapper::toResponseDto);
    }


    @Transactional(readOnly = true)
    public GradeResponseDto getGradeById(
            Long id) {

        Grade grade =
                gradeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Grade not found with id: "
                                                + id
                                )
                        );

        return gradeMapper.toResponseDto(
                grade
        );
    }


    public GradeResponseDto updateGrade(
            Long id,
            GradeRequestDto dto) {

        Grade grade =
                gradeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Grade not found with id: "
                                                + id
                                )
                        );

        Enrollment enrollment =
                enrollmentRepository
                        .findById(dto.getEnrollmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Enrollment not found with id: "
                                                + dto.getEnrollmentId()
                                )
                        );

        Exam exam =
                examRepository
                        .findById(dto.getExamId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Exam not found with id: "
                                                + dto.getExamId()
                                )
                        );


        if (gradeRepository
                .existsByEnrollmentIdAndExamIdAndIdNot(
                        dto.getEnrollmentId(),
                        dto.getExamId(),
                        id)) {

            throw new BadRequestException(
                    "Invalid Data: Grade already exists for this enrollment and exam"
            );
        }


        if (enrollment.getCourse() == null
                || exam.getCourse() == null
                || !enrollment.getCourse().getId()
                        .equals(exam.getCourse().getId())) {

            throw new BadRequestException(
                    "Exam course does not match enrollment course"
            );
        }


        if (dto.getMarksObtained() > exam.getTotalMarks()) {

            throw new BadRequestException(
                    "Marks obtained cannot be greater than total marks"
            );
        }


        gradeMapper.updateEntity(
                grade,
                dto,
                enrollment,
                exam
        );

        Grade updatedGrade =
                gradeRepository.save(grade);

        return gradeMapper.toResponseDto(
                updatedGrade
        );
    }


    public void deleteGrade(Long id) {

        Grade grade =
                gradeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Grade not found with id: "
                                                + id
                                )
                        );

        gradeRepository.delete(grade);
    }


    @Transactional(readOnly = true)
    public Page<GradeResponseDto> searchGrades(
            Long enrollmentId,
            Long examId,
            String grade,
            Pageable pageable) {

        boolean hasEnrollmentId =
                enrollmentId != null;

        boolean hasExamId =
                examId != null;

        boolean hasGrade =
                grade != null
                        && !grade.trim().isEmpty();


        if (hasEnrollmentId && hasExamId) {

            return gradeRepository
                    .findByEnrollmentIdAndExamId(
                            enrollmentId,
                            examId,
                            pageable
                    )
                    .map(gradeMapper::toResponseDto);
        }


        if (hasEnrollmentId) {

            return gradeRepository
                    .findByEnrollmentId(
                            enrollmentId,
                            pageable
                    )
                    .map(gradeMapper::toResponseDto);
        }


        if (hasExamId) {

            return gradeRepository
                    .findByExamId(
                            examId,
                            pageable
                    )
                    .map(gradeMapper::toResponseDto);
        }


        if (hasGrade) {

            return gradeRepository
                    .findByGradeIgnoreCase(
                            grade.trim(),
                            pageable
                    )
                    .map(gradeMapper::toResponseDto);
        }


        return gradeRepository
                .findAll(pageable)
                .map(gradeMapper::toResponseDto);
    }
}