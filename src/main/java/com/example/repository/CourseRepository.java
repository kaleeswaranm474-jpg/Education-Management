package com.example.repository;

import com.example.entity.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository
        extends JpaRepository<Course, Long> {

    boolean existsByCourseCode(String courseCode);

    Page<Course> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable);

    Page<Course> findByTeacherId(
            Long teacherId,
            Pageable pageable);

    Page<Course> findByNameContainingIgnoreCaseAndTeacherId(
            String name,
            Long teacherId,
            Pageable pageable);

    @Query(
        value = "SELECT c FROM Course c JOIN FETCH c.teacher",
        countQuery = "SELECT COUNT(c) FROM Course c"
    )
    Page<Course> findAllWithTeacher(Pageable pageable);
}