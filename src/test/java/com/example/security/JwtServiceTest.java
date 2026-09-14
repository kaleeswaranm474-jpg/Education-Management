package com.example.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;


    @BeforeEach
    void setUp() throws Exception {

        jwtService =
                new JwtService();


        Field secretField =
                JwtService.class
                        .getDeclaredField(
                                "secretKey"
                        );

        secretField.setAccessible(true);

        secretField.set(
                jwtService,
                "ThisIsAVeryStrongJwtSecretKey123456789"
        );


        Field expirationField =
                JwtService.class
                        .getDeclaredField(
                                "expirationTime"
                        );

        expirationField.setAccessible(true);

        expirationField.setLong(
                jwtService,
                3600000L
        );


        jwtService.init();
    }


    @Test
    void generateTokenShouldWork() {

        String token =
                jwtService.generateToken(
                        "admin",
                        "ADMIN"
                );

        assertNotNull(token);

        assertFalse(token.isBlank());
    }


    @Test
    void extractUsernameShouldWork() {

        String token =
                jwtService.generateToken(
                        "admin",
                        "ADMIN"
                );


        String username =
                jwtService.extractUsername(
                        token
                );


        assertEquals(
                "admin",
                username
        );
    }


    @Test
    void extractRoleShouldWork() {

        String token =
                jwtService.generateToken(
                        "teacher",
                        "TEACHER"
                );


        String role =
                jwtService.extractRole(
                        token
                );


        assertEquals(
                "TEACHER",
                role
        );
    }


    @Test
    void tokenShouldBeValid() {

        String token =
                jwtService.generateToken(
                        "student",
                        "STUDENT"
                );


        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "student"
                );


        assertTrue(valid);
    }


    @Test
    void tokenShouldBeInvalidForDifferentUsername() {

        String token =
                jwtService.generateToken(
                        "student",
                        "STUDENT"
                );


        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "admin"
                );


        assertFalse(valid);
    }
}