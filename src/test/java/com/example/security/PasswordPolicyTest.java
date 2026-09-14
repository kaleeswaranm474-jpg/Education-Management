package com.example.security;

import com.example.exception.BadRequestException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordPolicyTest {

    private final PasswordPolicy passwordPolicy =
            new PasswordPolicy();


    @Test
    void validPasswordShouldPass() {

        assertDoesNotThrow(() ->
                passwordPolicy.validate(
                        "Admin@12345"
                )
        );
    }


    @Test
    void passwordWithoutUppercaseShouldFail() {

        assertThrows(
                BadRequestException.class,
                () ->
                        passwordPolicy.validate(
                                "admin@12345"
                        )
        );
    }


    @Test
    void passwordWithoutLowercaseShouldFail() {

        assertThrows(
                BadRequestException.class,
                () ->
                        passwordPolicy.validate(
                                "ADMIN@12345"
                        )
        );
    }


    @Test
    void passwordWithoutNumberShouldFail() {

        assertThrows(
                BadRequestException.class,
                () ->
                        passwordPolicy.validate(
                                "Admin@abcdef"
                        )
        );
    }


    @Test
    void passwordWithoutSpecialCharacterShouldFail() {

        assertThrows(
                BadRequestException.class,
                () ->
                        passwordPolicy.validate(
                                "Admin12345"
                        )
        );
    }


    @Test
    void shortPasswordShouldFail() {

        assertThrows(
                BadRequestException.class,
                () ->
                        passwordPolicy.validate(
                                "Ad@123"
                        )
        );
    }
}