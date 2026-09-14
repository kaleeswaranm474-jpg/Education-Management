package com.example.service;

import com.example.dto.CourseRequestDto;
import com.example.dto.CourseResponseDto;
import com.example.entity.Course;
import com.example.entity.Teacher;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CourseMapper;
import com.example.repository.CourseRepository;
import com.example.repository.TeacherRepository;

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
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Teacher teacher;
    private CourseRequestDto requestDto;
    private CourseResponseDto responseDto;

    @BeforeEach
    void setUp() {

        teacher = new Teacher();

        teacher.setId(1L);
        teacher.setName("Arun Kumar");
        teacher.setEmail("arun@gmail.com");
        teacher.setPhone("9876543210");
        teacher.setDepartment("Computer Science");

        course = new Course();

        course.setId(1L);
        course.setName("Java Programming");
        course.setCourseCode("JAVA101");
        course.setDescription(
                "Core Java Programming Course"
        );
        course.setTeacher(teacher);

        requestDto = new CourseRequestDto();

        requestDto.setName("Java Programming");
        requestDto.setCourseCode("JAVA101");
        requestDto.setDescription(
                "Core Java Programming Course"
        );
        requestDto.setTeacherId(1L);

        responseDto = new CourseResponseDto(
                1L,
                "Java Programming",
                "JAVA101",
                "Core Java Programming Course",
                1L,
                "Arun Kumar"
        );
    }

    @Test
    void createCourseSuccess() {

        when(courseRepository.existsByCourseCode(
                requestDto.getCourseCode()
        )).thenReturn(false);

        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        when(courseMapper.toEntity(
                requestDto,
                teacher
        )).thenReturn(course);

        when(courseRepository.save(course))
                .thenReturn(course);

        when(courseMapper.toResponseDto(course))
                .thenReturn(responseDto);

        CourseResponseDto result =
                courseService.createCourse(requestDto);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Java Programming",
                result.getName()
        );

        assertEquals(
                "JAVA101",
                result.getCourseCode()
        );

        assertEquals(
                "Arun Kumar",
                result.getTeacherName()
        );

        verify(courseRepository)
                .existsByCourseCode(
                        requestDto.getCourseCode()
                );

        verify(teacherRepository)
                .findById(1L);

        verify(courseMapper)
                .toEntity(
                        requestDto,
                        teacher
                );

        verify(courseRepository)
                .save(course);

        verify(courseMapper)
                .toResponseDto(course);
    }

    @Test
    void createCourseDuplicateCourseCode() {

        when(courseRepository.existsByCourseCode(
                requestDto.getCourseCode()
        )).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> courseService.createCourse(requestDto)
        );

        verify(courseRepository)
                .existsByCourseCode(
                        requestDto.getCourseCode()
                );

        verify(teacherRepository, never())
                .findById(anyLong());

        verify(courseRepository, never())
                .save(any(Course.class));
    }

    @Test
    void createCourseTeacherNotFound() {

        when(courseRepository.existsByCourseCode(
                requestDto.getCourseCode()
        )).thenReturn(false);

        when(teacherRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.createCourse(requestDto)
        );

        verify(courseRepository)
                .existsByCourseCode(
                        requestDto.getCourseCode()
                );

        verify(teacherRepository)
                .findById(1L);

        verify(courseRepository, never())
                .save(any(Course.class));
    }

    @Test
    void getAllCoursesSuccess() {

        Teacher teacher2 = new Teacher();

        teacher2.setId(2L);
        teacher2.setName("Priya Devi");
        teacher2.setEmail("priya@gmail.com");
        teacher2.setPhone("9876543211");
        teacher2.setDepartment("Information Technology");

        Course course2 = new Course();

        course2.setId(2L);
        course2.setName("Database Management");
        course2.setCourseCode("DB101");
        course2.setDescription(
                "SQL and Database Management Course"
        );
        course2.setTeacher(teacher2);

        CourseResponseDto responseDto2 =
                new CourseResponseDto(
                        2L,
                        "Database Management",
                        "DB101",
                        "SQL and Database Management Course",
                        2L,
                        "Priya Devi"
                );

        List<Course> courses =
                Arrays.asList(course, course2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Course> coursePage =
                new PageImpl<>(courses, pageable, courses.size());

        when(courseRepository.findAll(pageable))
                .thenReturn(coursePage);

        when(courseMapper.toResponseDto(course))
                .thenReturn(responseDto);

        when(courseMapper.toResponseDto(course2))
                .thenReturn(responseDto2);

        Page<CourseResponseDto> result =
                courseService.getAllCourses(pageable);

        assertNotNull(result);

        assertEquals(
                2,
                result.getContent().size()
        );

        assertEquals(
                "Java Programming",
                result.getContent().get(0).getName()
        );

        assertEquals(
                "Database Management",
                result.getContent().get(1).getName()
        );

        verify(courseRepository)
                .findAll(pageable);

        verify(courseMapper)
                .toResponseDto(course);

        verify(courseMapper)
                .toResponseDto(course2);
    }

    @Test
    void getCourseByIdSuccess() {

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(courseMapper.toResponseDto(course))
                .thenReturn(responseDto);

        CourseResponseDto result =
                courseService.getCourseById(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Java Programming",
                result.getName()
        );

        assertEquals(
                "JAVA101",
                result.getCourseCode()
        );

        verify(courseRepository)
                .findById(1L);

        verify(courseMapper)
                .toResponseDto(course);
    }

    @Test
    void getCourseByIdNotFound() {

        when(courseRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.getCourseById(100L)
        );

        verify(courseRepository)
                .findById(100L);

        verify(courseMapper, never())
                .toResponseDto(any(Course.class));
    }

    @Test
    void updateCourseSuccess() {

        CourseRequestDto updateDto =
                new CourseRequestDto();

        updateDto.setName("Advanced Java");
        updateDto.setCourseCode("JAVA201");
        updateDto.setDescription(
                "Advanced Java Programming Course"
        );
        updateDto.setTeacherId(1L);

        CourseResponseDto updatedResponse =
                new CourseResponseDto(
                        1L,
                        "Advanced Java",
                        "JAVA201",
                        "Advanced Java Programming Course",
                        1L,
                        "Arun Kumar"
                );

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(courseRepository.existsByCourseCode(
                updateDto.getCourseCode()
        )).thenReturn(false);

        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        when(courseRepository.save(course))
                .thenReturn(course);

        when(courseMapper.toResponseDto(course))
                .thenReturn(updatedResponse);

        CourseResponseDto result =
                courseService.updateCourse(
                        1L,
                        updateDto
                );

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Advanced Java",
                result.getName()
        );

        assertEquals(
                "JAVA201",
                result.getCourseCode()
        );

        assertEquals(
                "Arun Kumar",
                result.getTeacherName()
        );

        verify(courseRepository)
                .findById(1L);

        verify(courseRepository)
                .existsByCourseCode(
                        updateDto.getCourseCode()
                );

        verify(teacherRepository)
                .findById(1L);

        verify(courseMapper)
                .updateEntity(
                        course,
                        updateDto,
                        teacher
                );

        verify(courseRepository)
                .save(course);

        verify(courseMapper)
                .toResponseDto(course);
    }

    @Test
    void updateCourseDuplicateCourseCode() {

        CourseRequestDto updateDto =
                new CourseRequestDto();

        updateDto.setName("Updated Course");
        updateDto.setCourseCode("DB101");
        updateDto.setDescription(
                "Updated Description"
        );
        updateDto.setTeacherId(1L);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(courseRepository.existsByCourseCode(
                updateDto.getCourseCode()
        )).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> courseService.updateCourse(
                        1L,
                        updateDto
                )
        );

        verify(courseRepository)
                .findById(1L);

        verify(courseRepository)
                .existsByCourseCode(
                        updateDto.getCourseCode()
                );

        verify(teacherRepository, never())
                .findById(anyLong());

        verify(courseRepository, never())
                .save(any(Course.class));

        verify(courseMapper, never())
                .updateEntity(
                        any(Course.class),
                        any(CourseRequestDto.class),
                        any(Teacher.class)
                );
    }

    @Test
    void deleteCourseSuccess() {

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository)
                .findById(1L);

        verify(courseRepository)
                .delete(course);
    }

    @Test
    void deleteCourseNotFound() {

        when(courseRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.deleteCourse(100L)
        );

        verify(courseRepository)
                .findById(100L);

        verify(courseRepository, never())
                .delete(any(Course.class));
    }
}