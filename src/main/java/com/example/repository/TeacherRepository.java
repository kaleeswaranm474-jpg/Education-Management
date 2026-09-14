package com.example.repository;

import com.example.entity.Teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByEmail(String email);

    Page<Teacher> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable);

    Page<Teacher> findByDepartmentIgnoreCase(
            String department,
            Pageable pageable);

    Page<Teacher> findByNameContainingIgnoreCaseAndDepartmentIgnoreCase(
            String name,
            String department,
            Pageable pageable);
}