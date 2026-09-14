package com.example.service;

import com.example.dto.GradeRequestDto;
import com.example.dto.GradeResponseDto;
import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Exam;
import com.example.entity.Grade;
import com.example.entity.Student;
import com.example.mapper.GradeMapper;
import com.example.repository.EnrollmentRepository;
import com.example.repository.ExamRepository;
import com.example.repository.GradeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private GradeMapper gradeMapper;

    @InjectMocks
    private GradeService gradeService;

    private Grade grade;
    private GradeRequestDto requestDto;
    private GradeResponseDto responseDto;

    private Enrollment enrollment;
    private Exam exam;
    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {

        student = new Student();
        student.setId(1L);
        student.setName("Arun Kumar");

        course = new Course();
        course.setId(1L);
        course.setName("Java Programming");

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        exam = new Exam();
        exam.setId(1L);
        exam.setExamName("Java Programming Exam");
        exam.setCourse(course);
        exam.setTotalMarks(100);

        grade = new Grade();
        grade.setId(1L);
        grade.setEnrollment(enrollment);
        grade.setExam(exam);
        grade.setMarksObtained(85.0);
        grade.setGrade("A");

        requestDto = new GradeRequestDto(
                1L,
                1L,
                85.0,
                "A"
        );

        responseDto = new GradeResponseDto(
                1L,
                1L,
                1L,
                "Java Programming Exam",
                "Arun Kumar",
                85.0,
                "A"
        );
    }

    // 1. CREATE GRADE - SUCCESS
    @Test
    void createGrade_success() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        when(gradeMapper.toEntity(
                requestDto,
                enrollment,
                exam))
                .thenReturn(grade);

        when(gradeRepository.save(grade))
                .thenReturn(grade);

        when(gradeMapper.toResponseDto(grade))
                .thenReturn(responseDto);

        GradeResponseDto result =
                gradeService.createGrade(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getEnrollmentId());
        assertEquals(1L, result.getExamId());
        assertEquals(
                "Java Programming Exam",
                result.getExamName()
        );
        assertEquals(
                "Arun Kumar",
                result.getStudentName()
        );
        assertEquals(85.0, result.getMarksObtained());
        assertEquals("A", result.getGrade());

        verify(gradeRepository)
                .save(grade);
    }

    // 2. CREATE GRADE - ENROLLMENT NOT FOUND
    @Test
    void createGrade_enrollmentNotFound() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .createGrade(requestDto)
                );

        assertNotNull(exception);

        verify(examRepository, never())
                .findById(anyLong());

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 3. CREATE GRADE - EXAM NOT FOUND
    @Test
    void createGrade_examNotFound() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .createGrade(requestDto)
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 4. CREATE GRADE - COURSE MISMATCH
    @Test
    void createGrade_courseMismatch() {

        Course anotherCourse = new Course();
        anotherCourse.setId(2L);
        anotherCourse.setName("Database Management");

        Exam anotherExam = new Exam();
        anotherExam.setId(2L);
        anotherExam.setExamName(
                "Database Management Exam"
        );
        anotherExam.setCourse(anotherCourse);
        anotherExam.setTotalMarks(100);

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(2L))
                .thenReturn(Optional.of(anotherExam));

        GradeRequestDto mismatchRequest =
                new GradeRequestDto(
                        1L,
                        2L,
                        85.0,
                        "A"
                );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .createGrade(mismatchRequest)
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 5. CREATE GRADE - MARKS GREATER THAN TOTAL MARKS
    @Test
    void createGrade_marksGreaterThanTotalMarks() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        GradeRequestDto invalidRequest =
                new GradeRequestDto(
                        1L,
                        1L,
                        120.0,
                        "A"
                );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .createGrade(invalidRequest)
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 6. GET ALL GRADES
    @Test
    void getAllGrades_success() {

        Grade grade2 = new Grade();
        grade2.setId(2L);
        grade2.setEnrollment(enrollment);
        grade2.setExam(exam);
        grade2.setMarksObtained(78.0);
        grade2.setGrade("B+");

        List<Grade> grades =
                Arrays.asList(grade, grade2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Grade> gradePage =
                new PageImpl<>(grades, pageable, grades.size());

        when(gradeRepository.findAll(pageable))
                .thenReturn(gradePage);

        when(gradeMapper.toResponseDto(grade))
                .thenReturn(responseDto);

        GradeResponseDto response2 =
                new GradeResponseDto(
                        2L,
                        1L,
                        1L,
                        "Java Programming Exam",
                        "Arun Kumar",
                        78.0,
                        "B+"
                );

        when(gradeMapper.toResponseDto(grade2))
                .thenReturn(response2);

        Page<GradeResponseDto> result =
                gradeService.getAllGrades(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        verify(gradeRepository)
                .findAll(pageable);
    }

    // 7. GET GRADE BY ID - SUCCESS
    @Test
    void getGradeById_success() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        when(gradeMapper.toResponseDto(grade))
                .thenReturn(responseDto);

        GradeResponseDto result =
                gradeService.getGradeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(85.0, result.getMarksObtained());
        assertEquals("A", result.getGrade());

        verify(gradeRepository)
                .findById(1L);
    }

    // 8. GET GRADE BY ID - NOT FOUND
    @Test
    void getGradeById_notFound() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .getGradeById(1L)
                );

        assertNotNull(exception);

        verify(gradeMapper, never())
                .toResponseDto(any(Grade.class));
    }

    // 9. UPDATE GRADE - SUCCESS
    @Test
    void updateGrade_success() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        when(gradeRepository.save(grade))
                .thenReturn(grade);

        when(gradeMapper.toResponseDto(grade))
                .thenReturn(responseDto);

        GradeResponseDto result =
                gradeService.updateGrade(
                        1L,
                        requestDto
                );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(85.0, result.getMarksObtained());
        assertEquals("A", result.getGrade());

        verify(gradeMapper)
                .updateEntity(
                        grade,
                        requestDto,
                        enrollment,
                        exam
                );

        verify(gradeRepository)
                .save(grade);
    }

    // 10. UPDATE GRADE - GRADE NOT FOUND
    @Test
    void updateGrade_gradeNotFound() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .updateGrade(
                                        1L,
                                        requestDto
                                )
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 11. UPDATE GRADE - ENROLLMENT NOT FOUND
    @Test
    void updateGrade_enrollmentNotFound() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .updateGrade(
                                        1L,
                                        requestDto
                                )
                );

        assertNotNull(exception);

        verify(examRepository, never())
                .findById(anyLong());

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 12. UPDATE GRADE - EXAM NOT FOUND
    @Test
    void updateGrade_examNotFound() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .updateGrade(
                                        1L,
                                        requestDto
                                )
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 13. UPDATE GRADE - COURSE MISMATCH
    @Test
    void updateGrade_courseMismatch() {

        Course anotherCourse = new Course();
        anotherCourse.setId(2L);
        anotherCourse.setName("Database Management");

        Exam anotherExam = new Exam();
        anotherExam.setId(2L);
        anotherExam.setExamName(
                "Database Management Exam"
        );
        anotherExam.setCourse(anotherCourse);
        anotherExam.setTotalMarks(100);

        GradeRequestDto mismatchRequest =
                new GradeRequestDto(
                        1L,
                        2L,
                        85.0,
                        "A"
                );

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(2L))
                .thenReturn(Optional.of(anotherExam));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .updateGrade(
                                        1L,
                                        mismatchRequest
                                )
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 14. UPDATE GRADE - MARKS GREATER THAN TOTAL MARKS
    @Test
    void updateGrade_marksGreaterThanTotalMarks() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        GradeRequestDto invalidRequest =
                new GradeRequestDto(
                        1L,
                        1L,
                        150.0,
                        "A"
                );

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .updateGrade(
                                        1L,
                                        invalidRequest
                                )
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .save(any(Grade.class));
    }

    // 15. DELETE GRADE - SUCCESS
    @Test
    void deleteGrade_success() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.of(grade));

        doNothing()
                .when(gradeRepository)
                .delete(grade);

        assertDoesNotThrow(
                () -> gradeService
                        .deleteGrade(1L)
        );

        verify(gradeRepository)
                .findById(1L);

        verify(gradeRepository)
                .delete(grade);
    }

    // 16. DELETE GRADE - NOT FOUND
    @Test
    void deleteGrade_notFound() {

        when(gradeRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> gradeService
                                .deleteGrade(1L)
                );

        assertNotNull(exception);

        verify(gradeRepository, never())
                .delete(any(Grade.class));
    }
}