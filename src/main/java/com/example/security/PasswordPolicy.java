package com.example.security;

import com.example.exception.BadRequestException;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordPolicy {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(
                    "^(?=.*[a-z])"
                            + "(?=.*[A-Z])"
                            + "(?=.*\\d)"
                            + "(?=.*[@#$%^&+=!])"
                            + ".{8,}$"
            );


    public void validate(String password) {

        if (password == null
                || !PASSWORD_PATTERN.matcher(password).matches()) {

            throw new BadRequestException(
                    "Password must contain at least 8 characters, "
                            + "one uppercase letter, "
                            + "one lowercase letter, "
                            + "one number, "
                            + "and one special character"
            );
        }
    }
}