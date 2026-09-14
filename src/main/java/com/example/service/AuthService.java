package com.example.service;

import com.example.dto.LoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.PasswordChangeRequestDto;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.repository.UserRepository;
import com.example.security.JwtService;
import com.example.security.PasswordPolicy;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordPolicy passwordPolicy;


    public LoginResponseDto login(
            LoginRequestDto dto) {

        User user =
                userRepository
                        .findByUsername(dto.getUsername())
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Invalid username or password"
                                )
                        );


        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword())) {

            throw new BadRequestException(
                    "Invalid username or password"
            );
        }


        String token =
                jwtService.generateToken(
                        user.getUsername(),
                        user.getRole()
                );


        return new LoginResponseDto(
                token,
                user.getUsername(),
                user.getRole()
        );
    }


    @PreAuthorize("isAuthenticated()")
    public void changePassword(
            PasswordChangeRequestDto dto) {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();


        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "User not found"
                                )
                        );


        if (!passwordEncoder.matches(
                dto.getCurrentPassword(),
                user.getPassword())) {

            throw new BadRequestException(
                    "Current password is incorrect"
            );
        }


        passwordPolicy.validate(
                dto.getNewPassword()
        );


        if (passwordEncoder.matches(
                dto.getNewPassword(),
                user.getPassword())) {

            throw new BadRequestException(
                    "New password must be different from current password"
            );
        }


        user.setPassword(
                passwordEncoder.encode(
                        dto.getNewPassword()
                )
        );


        userRepository.save(user);
    }
}