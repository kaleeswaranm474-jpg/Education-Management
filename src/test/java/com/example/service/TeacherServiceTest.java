package com.example.service;

import com.example.dto.TeacherRequestDto;
import com.example.dto.TeacherResponseDto;
import com.example.entity.Teacher;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.TeacherMapper;
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
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private TeacherRequestDto requestDto;
    private TeacherResponseDto responseDto;

    @BeforeEach
    void setUp() {

        teacher = new Teacher();

        teacher.setId(1L);
        teacher.setName("Arun Kumar");
        teacher.setEmail("arun@gmail.com");
        teacher.setPhone("9876543210");
        teacher.setDepartment("Computer Science");

        requestDto = new TeacherRequestDto();

        requestDto.setName("Arun Kumar");
        requestDto.setEmail("arun@gmail.com");
        requestDto.setPhone("9876543210");
        requestDto.setDepartment("Computer Science");

        responseDto = new TeacherResponseDto(
                1L,
                "Arun Kumar",
                "arun@gmail.com",
                "9876543210",
                "Computer Science"
        );
    }

    @Test
    void createTeacherSuccess() {

        when(teacherRepository.existsByEmail(
                requestDto.getEmail()
        )).thenReturn(false);

        when(teacherMapper.toEntity(requestDto))
                .thenReturn(teacher);

        when(teacherRepository.save(teacher))
                .thenReturn(teacher);

        when(teacherMapper.toResponseDto(teacher))
                .thenReturn(responseDto);

        TeacherResponseDto result =
                teacherService.createTeacher(requestDto);

        assertNotNull(result);

        assertEquals(
                "Arun Kumar",
                result.getName()
        );

        assertEquals(
                "arun@gmail.com",
                result.getEmail()
        );

        verify(teacherRepository)
                .existsByEmail(requestDto.getEmail());

        verify(teacherMapper)
                .toEntity(requestDto);

        verify(teacherRepository)
                .save(teacher);

        verify(teacherMapper)
                .toResponseDto(teacher);
    }

    @Test
    void createTeacherDuplicateEmail() {

        when(teacherRepository.existsByEmail(
                requestDto.getEmail()
        )).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> teacherService.createTeacher(requestDto)
        );

        verify(teacherRepository)
                .existsByEmail(requestDto.getEmail());

        verify(teacherRepository, never())
                .save(any(Teacher.class));

        verify(teacherMapper, never())
                .toEntity(any(TeacherRequestDto.class));
    }

    @Test
    void getAllTeachersSuccess() {

        Teacher teacher2 = new Teacher();

        teacher2.setId(2L);
        teacher2.setName("Priya Devi");
        teacher2.setEmail("priya@gmail.com");
        teacher2.setPhone("9876543211");
        teacher2.setDepartment("Information Technology");

        TeacherResponseDto responseDto2 =
                new TeacherResponseDto(
                        2L,
                        "Priya Devi",
                        "priya@gmail.com",
                        "9876543211",
                        "Information Technology"
                );

        List<Teacher> teachers =
                Arrays.asList(teacher, teacher2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Teacher> teacherPage =
                new PageImpl<>(teachers, pageable, teachers.size());

        when(teacherRepository.findAll(pageable))
                .thenReturn(teacherPage);

        when(teacherMapper.toResponseDto(teacher))
                .thenReturn(responseDto);

        when(teacherMapper.toResponseDto(teacher2))
                .thenReturn(responseDto2);

        Page<TeacherResponseDto> result =
                teacherService.getAllTeachers(pageable);

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

        verify(teacherRepository)
                .findAll(pageable);

        verify(teacherMapper)
                .toResponseDto(teacher);

        verify(teacherMapper)
                .toResponseDto(teacher2);
    }

    @Test
    void getTeacherByIdSuccess() {

        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        when(teacherMapper.toResponseDto(teacher))
                .thenReturn(responseDto);

        TeacherResponseDto result =
                teacherService.getTeacherById(1L);

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
                "arun@gmail.com",
                result.getEmail()
        );

        verify(teacherRepository)
                .findById(1L);

        verify(teacherMapper)
                .toResponseDto(teacher);
    }

    @Test
    void getTeacherByIdNotFound() {

        when(teacherRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> teacherService.getTeacherById(100L)
        );

        verify(teacherRepository)
                .findById(100L);

        verify(teacherMapper, never())
                .toResponseDto(any(Teacher.class));
    }

    @Test
    void updateTeacherSuccess() {

        TeacherRequestDto updateDto =
                new TeacherRequestDto();

        updateDto.setName("Arun Kumar Updated");
        updateDto.setEmail("arun.updated@gmail.com");
        updateDto.setPhone("9876543215");
        updateDto.setDepartment("Information Technology");

        TeacherResponseDto updatedResponse =
                new TeacherResponseDto(
                        1L,
                        "Arun Kumar Updated",
                        "arun.updated@gmail.com",
                        "9876543215",
                        "Information Technology"
                );

        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        when(teacherRepository.existsByEmail(
                updateDto.getEmail()
        )).thenReturn(false);

        when(teacherRepository.save(teacher))
                .thenReturn(teacher);

        when(teacherMapper.toResponseDto(teacher))
                .thenReturn(updatedResponse);

        TeacherResponseDto result =
                teacherService.updateTeacher(
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
                "9876543215",
                result.getPhone()
        );

        assertEquals(
                "Information Technology",
                result.getDepartment()
        );

        verify(teacherRepository)
                .findById(1L);

        verify(teacherRepository)
                .existsByEmail(updateDto.getEmail());

        verify(teacherMapper)
                .updateEntity(
                        teacher,
                        updateDto
                );

        verify(teacherRepository)
                .save(teacher);

        verify(teacherMapper)
                .toResponseDto(teacher);
    }

    @Test
    void updateTeacherDuplicateEmail() {

        TeacherRequestDto updateDto =
                new TeacherRequestDto();

        updateDto.setName("Arun Kumar");
        updateDto.setEmail("priya@gmail.com");
        updateDto.setPhone("9876543210");
        updateDto.setDepartment("Computer Science");

        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        when(teacherRepository.existsByEmail(
                updateDto.getEmail()
        )).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> teacherService.updateTeacher(
                        1L,
                        updateDto
                )
        );

        verify(teacherRepository)
                .findById(1L);

        verify(teacherRepository)
                .existsByEmail(updateDto.getEmail());

        verify(teacherRepository, never())
                .save(any(Teacher.class));

        verify(teacherMapper, never())
                .updateEntity(
                        any(Teacher.class),
                        any(TeacherRequestDto.class)
                );
    }

    @Test
    void deleteTeacherSuccess() {

        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));

        teacherService.deleteTeacher(1L);

        verify(teacherRepository)
                .findById(1L);

        verify(teacherRepository)
                .delete(teacher);
    }

    @Test
    void deleteTeacherNotFound() {

        when(teacherRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> teacherService.deleteTeacher(100L)
        );

        verify(teacherRepository)
                .findById(100L);

        verify(teacherRepository, never())
                .delete(any(Teacher.class));
    }
}