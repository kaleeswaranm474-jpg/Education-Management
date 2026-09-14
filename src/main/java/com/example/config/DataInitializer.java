package com.example.config;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.security.PasswordPolicy;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordPolicy passwordPolicy;

    @Value("${app.default-users.admin-password:Admin@12345}")
    private String adminPassword;

    @Value("${app.default-users.teacher-password:Teacher@12345}")
    private String teacherPassword;

    @Value("${app.default-users.student-password:Student@12345}")
    private String studentPassword;

    @Bean
    public CommandLineRunner createDefaultUsers() {

        return args -> {

            passwordPolicy.validate(adminPassword);
            passwordPolicy.validate(teacherPassword);
            passwordPolicy.validate(studentPassword);

            // Admin
            if (!userRepository.existsByUsername("admin")) {

                User admin = new User();

                admin.setUsername("admin");

                admin.setPassword(
                        passwordEncoder.encode(adminPassword)
                );

                admin.setRole("ADMIN");

                userRepository.save(admin);
            }

            // Teacher
            if (!userRepository.existsByUsername("teacher")) {

                User teacher = new User();

                teacher.setUsername("teacher");

                teacher.setPassword(
                        passwordEncoder.encode(teacherPassword)
                );

                teacher.setRole("TEACHER");

                userRepository.save(teacher);
            }

            // Student
            if (!userRepository.existsByUsername("student")) {

                User student = new User();

                student.setUsername("student");

                student.setPassword(
                        passwordEncoder.encode(studentPassword)
                );

                student.setRole("STUDENT");

                userRepository.save(student);
            }
        };
    }
}