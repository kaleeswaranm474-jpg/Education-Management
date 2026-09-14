package com.example.service;

import com.example.dto.EnrollmentRequestDto;
import com.example.dto.EnrollmentResponseDto;
import com.example.entity.Course;
import com.example.entity.Enrollment;
import com.example.entity.Student;
import com.example.mapper.EnrollmentMapper;
import com.example.repository.CourseRepository;
import com.example.repository.EnrollmentRepository;
import com.example.repository.StudentRepository;

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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private EnrollmentRequestDto requestDto;
    private EnrollmentResponseDto responseDto;

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
        enrollment.setEnrollmentDate(LocalDate.now());

        requestDto = new EnrollmentRequestDto(
                1L,
                1L
        );

        responseDto = new EnrollmentResponseDto(
                1L,
                1L,
                "Arun Kumar",
                1L,
                "Java Programming",
                LocalDate.now()
        );
    }

    // 1. CREATE ENROLLMENT - SUCCESS
    @Test
    void createEnrollment_success() {

        when(enrollmentRepository
                .existsByStudentIdAndCourseId(1L, 1L))
                .thenReturn(false);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentMapper.toEntity(
                requestDto,
                student,
                course))
                .thenReturn(enrollment);

        when(enrollmentRepository.save(enrollment))
                .thenReturn(enrollment);

        when(enrollmentMapper.toResponseDto(enrollment))
                .thenReturn(responseDto);

        EnrollmentResponseDto result =
                enrollmentService.createEnrollment(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());
        assertEquals("Arun Kumar", result.getStudentName());
        assertEquals("Java Programming", result.getCourseName());

        verify(enrollmentRepository)
                .save(enrollment);
    }

    // 2. CREATE ENROLLMENT - DUPLICATE
    @Test
    void createEnrollment_duplicateEnrollment() {

        when(enrollmentRepository
                .existsByStudentIdAndCourseId(1L, 1L))
                .thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService
                                .createEnrollment(requestDto)
                );

        assertNotNull(exception);

        verify(enrollmentRepository, never())
                .save(any(Enrollment.class));
    }

    // 3. CREATE ENROLLMENT - STUDENT NOT FOUND
    @Test
    void createEnrollment_studentNotFound() {

        when(enrollmentRepository
                .existsByStudentIdAndCourseId(1L, 1L))
                .thenReturn(false);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService
                                .createEnrollment(requestDto)
                );

        assertNotNull(exception);

        verify(courseRepository, never())
                .findById(anyLong());

        verify(enrollmentRepository, never())
                .save(any(Enrollment.class));
    }

    // 4. CREATE ENROLLMENT - COURSE NOT FOUND
    @Test
    void createEnrollment_courseNotFound() {

        when(enrollmentRepository
                .existsByStudentIdAndCourseId(1L, 1L))
                .thenReturn(false);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService
                                .createEnrollment(requestDto)
                );

        assertNotNull(exception);

        verify(enrollmentRepository, never())
                .save(any(Enrollment.class));
    }

    // 5. GET ALL ENROLLMENTS
    @Test
    void getAllEnrollments_success() {

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(2L);
        enrollment2.setStudent(student);
        enrollment2.setCourse(course);
        enrollment2.setEnrollmentDate(LocalDate.now());

        List<Enrollment> enrollments =
                Arrays.asList(enrollment, enrollment2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Enrollment> enrollmentPage =
                new PageImpl<>(enrollments, pageable, enrollments.size());

        when(enrollmentRepository.findAll(pageable))
                .thenReturn(enrollmentPage);

        when(enrollmentMapper.toResponseDto(enrollment))
                .thenReturn(responseDto);

        EnrollmentResponseDto response2 =
                new EnrollmentResponseDto(
                        2L,
                        1L,
                        "Arun Kumar",
                        1L,
                        "Java Programming",
                        LocalDate.now()
                );

        when(enrollmentMapper.toResponseDto(enrollment2))
                .thenReturn(response2);

        Page<EnrollmentResponseDto> result =
                enrollmentService.getAllEnrollments(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        verify(enrollmentRepository)
                .findAll(pageable);
    }

    // 6. GET ENROLLMENT BY ID - SUCCESS
    @Test
    void getEnrollmentById_success() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(enrollmentMapper.toResponseDto(enrollment))
                .thenReturn(responseDto);

        EnrollmentResponseDto result =
                enrollmentService.getEnrollmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Arun Kumar", result.getStudentName());
        assertEquals("Java Programming", result.getCourseName());

        verify(enrollmentRepository)
                .findById(1L);
    }

    // 7. GET ENROLLMENT BY ID - NOT FOUND
    @Test
    void getEnrollmentById_notFound() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService
                                .getEnrollmentById(1L)
                );

        assertNotNull(exception);

        verify(enrollmentMapper, never())
                .toResponseDto(any(Enrollment.class));
    }

    // 8. UPDATE ENROLLMENT - SUCCESS
    @Test
    void updateEnrollment_success() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepository.save(enrollment))
                .thenReturn(enrollment);

        when(enrollmentMapper.toResponseDto(enrollment))
                .thenReturn(responseDto);

        EnrollmentResponseDto result =
                enrollmentService.updateEnrollment(
                        1L,
                        requestDto
                );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());

        verify(enrollmentMapper)
                .updateEntity(
                        enrollment,
                        student,
                        course
                );

        verify(enrollmentRepository)
                .save(enrollment);
    }

    // 9. UPDATE ENROLLMENT - DUPLICATE
    @Test
    void updateEnrollment_duplicateEnrollment() {

        Student newStudent = new Student();
        newStudent.setId(2L);
        newStudent.setName("Priya Devi");

        Course newCourse = new Course();
        newCourse.setId(2L);
        newCourse.setName("Database Management");

        EnrollmentRequestDto newRequest =
                new EnrollmentRequestDto(
                        2L,
                        2L
                );

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        when(enrollmentRepository
                .existsByStudentIdAndCourseId(2L, 2L))
                .thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService
                                .updateEnrollment(
                                        1L,
                                        newRequest
                                )
                );

        assertNotNull(exception);

        verify(enrollmentRepository, never())
                .save(any(Enrollment.class));
    }

    // 10. DELETE ENROLLMENT - SUCCESS
    @Test
    void deleteEnrollment_success() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        doNothing()
                .when(enrollmentRepository)
                .delete(enrollment);

        assertDoesNotThrow(
                () -> enrollmentService
                        .deleteEnrollment(1L)
        );

        verify(enrollmentRepository)
                .findById(1L);

        verify(enrollmentRepository)
                .delete(enrollment);
    }

    // 11. DELETE ENROLLMENT - NOT FOUND
    @Test
    void deleteEnrollment_notFound() {

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService
                                .deleteEnrollment(1L)
                );

        assertNotNull(exception);

        verify(enrollmentRepository, never())
                .delete(any(Enrollment.class));
    }
}