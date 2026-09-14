package com.example.repository;

import com.example.entity.Grade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository
        extends JpaRepository<Grade, Long> {

    Page<Grade> findByEnrollmentId(
            Long enrollmentId,
            Pageable pageable);

    Page<Grade> findByExamId(
            Long examId,
            Pageable pageable);

    Page<Grade> findByGradeIgnoreCase(
            String grade,
            Pageable pageable);

    Page<Grade> findByEnrollmentIdAndExamId(
            Long enrollmentId,
            Long examId,
            Pageable pageable);
}