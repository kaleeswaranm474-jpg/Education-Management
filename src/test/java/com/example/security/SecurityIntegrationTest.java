package com.example.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedRequestShouldReturn401() throws Exception {

        mockMvc.perform(
                get("/api/students")
        )
        .andExpect(status().isUnauthorized());
    }

    @Test
    void studentCanViewStudents() throws Exception {

        mockMvc.perform(
                get("/api/students")
                        .with(user("student@test.com")
                                .roles("STUDENT"))
        )
        .andExpect(status().isOk());
    }

    @Test
    void studentCannotViewTeachers() throws Exception {

        mockMvc.perform(
                get("/api/teachers")
                        .with(user("student@test.com")
                                .roles("STUDENT"))
        )
        .andExpect(status().isForbidden());
    }

    @Test
    void studentCannotViewCourses() throws Exception {

        mockMvc.perform(
                get("/api/courses")
                        .with(user("student@test.com")
                                .roles("STUDENT"))
        )
        .andExpect(status().isForbidden());
    }
}