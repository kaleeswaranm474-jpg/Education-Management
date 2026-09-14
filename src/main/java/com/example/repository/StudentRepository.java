package com.example.repository;

import com.example.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Student> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Student> findByDepartmentIgnoreCase(
            String department,
            Pageable pageable
    );

    Page<Student> findByNameContainingIgnoreCaseAndDepartmentIgnoreCase(
            String name,
            String department,
            Pageable pageable
    );
}