package com.example.service;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.entity.Course;
import com.example.entity.Exam;
import com.example.mapper.ExamMapper;
import com.example.repository.CourseRepository;
import com.example.repository.ExamRepository;

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
class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ExamMapper examMapper;

    @InjectMocks
    private ExamService examService;

    private Exam exam;
    private ExamRequestDto requestDto;
    private ExamResponseDto responseDto;

    private Course course;

    @BeforeEach
    void setUp() {

        course = new Course();
        course.setId(1L);
        course.setName("Java Programming");

        exam = new Exam();
        exam.setId(1L);
        exam.setExamName("Java Programming Exam");
        exam.setCourse(course);
        exam.setExamDate(LocalDate.of(2026, 9, 15));
        exam.setTotalMarks(100);

        requestDto = new ExamRequestDto(
                "Java Programming Exam",
                1L,
                LocalDate.of(2026, 9, 15),
                100
        );

        responseDto = new ExamResponseDto(
                1L,
                "Java Programming Exam",
                1L,
                "Java Programming",
                LocalDate.of(2026, 9, 15),
                100
        );
    }

    // 1. CREATE EXAM - SUCCESS
    @Test
    void createExam_success() {

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(examMapper.toEntity(
                requestDto,
                course))
                .thenReturn(exam);

        when(examRepository.save(exam))
                .thenReturn(exam);

        when(examMapper.toResponseDto(exam))
                .thenReturn(responseDto);

        ExamResponseDto result =
                examService.createExam(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(
                "Java Programming Exam",
                result.getExamName()
        );
        assertEquals(1L, result.getCourseId());
        assertEquals(
                "Java Programming",
                result.getCourseName()
        );
        assertEquals(100, result.getTotalMarks());

        verify(examRepository)
                .save(exam);
    }

    // 2. CREATE EXAM - COURSE NOT FOUND
    @Test
    void createExam_courseNotFound() {

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> examService
                                .createExam(requestDto)
                );

        assertNotNull(exception);

        verify(examRepository, never())
                .save(any(Exam.class));

        verify(examMapper, never())
                .toEntity(
                        any(ExamRequestDto.class),
                        any(Course.class)
                );
    }

    // 3. GET ALL EXAMS
    @Test
    void getAllExams_success() {

        Exam exam2 = new Exam();
        exam2.setId(2L);
        exam2.setExamName("Database Management Exam");
        exam2.setCourse(course);
        exam2.setExamDate(LocalDate.of(2026, 9, 18));
        exam2.setTotalMarks(100);

        List<Exam> exams =
                Arrays.asList(exam, exam2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Exam> examPage =
                new PageImpl<>(exams, pageable, exams.size());

        when(examRepository.findAll(pageable))
                .thenReturn(examPage);

        when(examMapper.toResponseDto(exam))
                .thenReturn(responseDto);

        ExamResponseDto response2 =
                new ExamResponseDto(
                        2L,
                        "Database Management Exam",
                        1L,
                        "Java Programming",
                        LocalDate.of(2026, 9, 18),
                        100
                );

        when(examMapper.toResponseDto(exam2))
                .thenReturn(response2);

        Page<ExamResponseDto> result =
                examService.getAllExams(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        verify(examRepository)
                .findAll(pageable);
    }

    // 4. GET EXAM BY ID - SUCCESS
    @Test
    void getExamById_success() {

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        when(examMapper.toResponseDto(exam))
                .thenReturn(responseDto);

        ExamResponseDto result =
                examService.getExamById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(
                "Java Programming Exam",
                result.getExamName()
        );
        assertEquals(1L, result.getCourseId());

        verify(examRepository)
                .findById(1L);
    }

    // 5. GET EXAM BY ID - NOT FOUND
    @Test
    void getExamById_notFound() {

        when(examRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> examService
                                .getExamById(1L)
                );

        assertNotNull(exception);

        verify(examMapper, never())
                .toResponseDto(any(Exam.class));
    }

    // 6. UPDATE EXAM - SUCCESS
    @Test
    void updateExam_success() {

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(examRepository.save(exam))
                .thenReturn(exam);

        when(examMapper.toResponseDto(exam))
                .thenReturn(responseDto);

        ExamResponseDto result =
                examService.updateExam(
                        1L,
                        requestDto
                );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(
                "Java Programming Exam",
                result.getExamName()
        );
        assertEquals(100, result.getTotalMarks());

        verify(examMapper)
                .updateEntity(
                        exam,
                        requestDto,
                        course
                );

        verify(examRepository)
                .save(exam);
    }

    // 7. UPDATE EXAM - EXAM NOT FOUND
    @Test
    void updateExam_examNotFound() {

        when(examRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> examService
                                .updateExam(
                                        1L,
                                        requestDto
                                )
                );

        assertNotNull(exception);

        verify(examRepository, never())
                .save(any(Exam.class));
    }

    // 8. UPDATE EXAM - COURSE NOT FOUND
    @Test
    void updateExam_courseNotFound() {

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> examService
                                .updateExam(
                                        1L,
                                        requestDto
                                )
                );

        assertNotNull(exception);

        verify(examRepository, never())
                .save(any(Exam.class));

        verify(examMapper, never())
                .updateEntity(
                        any(Exam.class),
                        any(ExamRequestDto.class),
                        any(Course.class)
                );
    }

    // 9. DELETE EXAM - SUCCESS
    @Test
    void deleteExam_success() {

        when(examRepository.findById(1L))
                .thenReturn(Optional.of(exam));

        doNothing()
                .when(examRepository)
                .delete(exam);

        assertDoesNotThrow(
                () -> examService
                        .deleteExam(1L)
        );

        verify(examRepository)
                .findById(1L);

        verify(examRepository)
                .delete(exam);
    }

    // 10. DELETE EXAM - NOT FOUND
    @Test
    void deleteExam_notFound() {

        when(examRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> examService
                                .deleteExam(1L)
                );

        assertNotNull(exception);

        verify(examRepository, never())
                .delete(any(Exam.class));
    }
}