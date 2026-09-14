package com.example.repository;

import com.example.entity.Exam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository
        extends JpaRepository<Exam, Long> {

    Page<Exam> findByExamNameContainingIgnoreCase(
            String examName,
            Pageable pageable);

    Page<Exam> findByCourseId(
            Long courseId,
            Pageable pageable);

    Page<Exam> findByExamNameContainingIgnoreCaseAndCourseId(
            String examName,
            Long courseId,
            Pageable pageable);

    boolean existsByExamNameIgnoreCaseAndCourseIdAndExamDate(
            String examName,
            Long courseId,
            java.time.LocalDate examDate);

    boolean existsByExamNameIgnoreCaseAndCourseIdAndExamDateAndIdNot(
            String examName,
            Long courseId,
            java.time.LocalDate examDate,
            Long id);
}