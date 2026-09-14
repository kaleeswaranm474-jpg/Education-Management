package com.example.service;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.entity.Student;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.StudentMapper;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentRequestDto requestDto;
    private StudentResponseDto responseDto;

    @BeforeEach
    void setUp() {

        student = new Student();

        student.setId(1L);
        student.setName("Arun Kumar");
        student.setEmail("arun.student@gmail.com");
        student.setPhone("9876500002");
        student.setDepartment("IT");

        requestDto = new StudentRequestDto();

        requestDto.setName("Arun Kumar");
        requestDto.setEmail("arun.student@gmail.com");
        requestDto.setPhone("9876500002");
        requestDto.setDepartment("IT");

        responseDto = new StudentResponseDto(
                1L,
                "Arun Kumar",
                "arun.student@gmail.com",
                "9876500002",
                "IT"
        );
    }

    @Test
    void createStudentSuccess() {

        when(studentRepository.existsByEmail(
                requestDto.getEmail()
        )).thenReturn(false);

        when(studentMapper.toEntity(requestDto))
                .thenReturn(student);

        when(studentRepository.save(student))
                .thenReturn(student);

        when(studentMapper.toResponseDto(student))
                .thenReturn(responseDto);

        StudentResponseDto result =
                studentService.createStudent(requestDto);

        assertNotNull(result);

        assertEquals(
                "Arun Kumar",
                result.getName()
        );

        assertEquals(
                "arun.student@gmail.com",
                result.getEmail()
        );

        verify(studentRepository)
                .existsByEmail(requestDto.getEmail());

        verify(studentMapper)
                .toEntity(requestDto);

        verify(studentRepository)
                .save(student);

        verify(studentMapper)
                .toResponseDto(student);
    }

    @Test
    void createStudentDuplicateEmail() {

        when(studentRepository.existsByEmail(
                requestDto.getEmail()
        )).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> studentService.createStudent(requestDto)
        );

        verify(studentRepository)
                .existsByEmail(requestDto.getEmail());

        verify(studentRepository, never())
                .save(any(Student.class));
    }

    @Test
    void getAllStudentsSuccess() {

        Student student2 = new Student();

        student2.setId(2L);
        student2.setName("Priya Devi");
        student2.setEmail("priya.student@gmail.com");
        student2.setPhone("9876500003");
        student2.setDepartment("Computer Science");

        StudentResponseDto responseDto2 =
                new StudentResponseDto(
                        2L,
                        "Priya Devi",
                        "priya.student@gmail.com",
                        "9876500003",
                        "Computer Science"
                );

        List<Student> students =
                Arrays.asList(student, student2);

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Student> studentPage =
                new PageImpl<>(
                        students,
                        pageable,
                        students.size()
                );

        when(studentRepository.findAll(pageable))
                .thenReturn(studentPage);

        when(studentMapper.toResponseDto(student))
                .thenReturn(responseDto);

        when(studentMapper.toResponseDto(student2))
                .thenReturn(responseDto2);

        Page<StudentResponseDto> result =
                studentService.getAllStudents(pageable);

        assertNotNull(result);

        assertEquals(
                2,
                result.getContent().size()
        );

        assertEquals(
                "Arun Kumar",
                result.getContent().get(0).getName()
        );

        assertEquals(
                "Priya Devi",
                result.getContent().get(1).getName()
        );

        assertEquals(
                0,
                result.getNumber()
        );

        assertEquals(
                10,
                result.getSize()
        );

        assertEquals(
                2,
                result.getTotalElements()
        );

        verify(studentRepository)
                .findAll(pageable);

        verify(studentMapper)
                .toResponseDto(student);

        verify(studentMapper)
                .toResponseDto(student2);
    }

    @Test
    void getStudentByIdSuccess() {

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(studentMapper.toResponseDto(student))
                .thenReturn(responseDto);

        StudentResponseDto result =
                studentService.getStudentById(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Arun Kumar",
                result.getName()
        );

        assertEquals(
                "arun.student@gmail.com",
                result.getEmail()
        );

        verify(studentRepository)
                .findById(1L);

        verify(studentMapper)
                .toResponseDto(student);
    }

    @Test
    void getStudentByIdNotFound() {

        when(studentRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> studentService.getStudentById(100L)
        );

        verify(studentRepository)
                .findById(100L);

        verify(studentMapper, never())
                .toResponseDto(any(Student.class));
    }

    @Test
    void updateStudentSuccess() {

        StudentRequestDto updateDto =
                new StudentRequestDto();

        updateDto.setName("Arun Kumar Updated");
        updateDto.setEmail("arun.updated@gmail.com");
        updateDto.setPhone("9876500010");
        updateDto.setDepartment("Computer Science");

        StudentResponseDto updatedResponse =
                new StudentResponseDto(
                        1L,
                        "Arun Kumar Updated",
                        "arun.updated@gmail.com",
                        "9876500010",
                        "Computer Science"
                );

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(studentRepository.existsByEmail(
                updateDto.getEmail()
        )).thenReturn(false);

        when(studentRepository.save(student))
                .thenReturn(student);

        when(studentMapper.toResponseDto(student))
                .thenReturn(updatedResponse);

        StudentResponseDto result =
                studentService.updateStudent(
                        1L,
                        updateDto
                );

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Arun Kumar Updated",
                result.getName()
        );

        assertEquals(
                "arun.updated@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "9876500010",
                result.getPhone()
        );

        assertEquals(
                "Computer Science",
                result.getDepartment()
        );

        verify(studentRepository)
                .findById(1L);

        verify(studentRepository)
                .existsByEmail(updateDto.getEmail());

        verify(studentMapper)
                .updateEntity(
                        student,
                        updateDto
                );

        verify(studentRepository)
                .save(student);

        verify(studentMapper)
                .toResponseDto(student);
    }

    @Test
    void updateStudentDuplicateEmail() {

        StudentRequestDto updateDto =
                new StudentRequestDto();

        updateDto.setName("Arun Kumar");
        updateDto.setEmail("priya.student@gmail.com");
        updateDto.setPhone("9876500002");
        updateDto.setDepartment("IT");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(studentRepository.existsByEmail(
                updateDto.getEmail()
        )).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> studentService.updateStudent(
                        1L,
                        updateDto
                )
        );

        verify(studentRepository)
                .findById(1L);

        verify(studentRepository)
                .existsByEmail(updateDto.getEmail());

        verify(studentRepository, never())
                .save(any(Student.class));

        verify(studentMapper, never())
                .updateEntity(
                        any(Student.class),
                        any(StudentRequestDto.class)
                );
    }

    @Test
    void deleteStudentSuccess() {

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository)
                .findById(1L);

        verify(studentRepository)
                .delete(student);
    }

    @Test
    void deleteStudentNotFound() {

        when(studentRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> studentService.deleteStudent(100L)
        );

        verify(studentRepository)
                .findById(100L);

        verify(studentRepository, never())
                .delete(any(Student.class));
    }
}