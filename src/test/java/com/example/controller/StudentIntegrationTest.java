package com.example.controller;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.service.StudentService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@WithMockUser(
        username = "test-user",
        roles = "ADMIN"
)
class StudentIntegrationTest {

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentService studentService;


    @Test
    void integrationTest_applicationContextAndBeans() {

        assertNotNull(studentController);

        assertNotNull(studentService);
    }


    @Test
    void integrationTest_createAndGetStudent() {

        StudentRequestDto request =
                new StudentRequestDto(
                        "Integration Test Student",
                        "integration.test@gmail.com",
                        "9876500099",
                        "Computer Science"
                );


        StudentResponseDto created =
                studentService.createStudent(
                        request
                );


        assertNotNull(created);

        assertNotNull(created.getId());

        assertEquals(
                "Integration Test Student",
                created.getName()
        );

        assertEquals(
                "integration.test@gmail.com",
                created.getEmail()
        );


        StudentResponseDto fetched =
                studentService.getStudentById(
                        created.getId()
                );


        assertNotNull(fetched);

        assertEquals(
                created.getId(),
                fetched.getId()
        );

        assertEquals(
                "Integration Test Student",
                fetched.getName()
        );

        assertEquals(
                "integration.test@gmail.com",
                fetched.getEmail()
        );
    }
}