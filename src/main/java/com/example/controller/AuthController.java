package com.example.controller;

import com.example.dto.LoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.PasswordChangeRequestDto;
import com.example.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }


    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody PasswordChangeRequestDto dto) {

        authService.changePassword(dto);

        return ResponseEntity.ok(
                "Password changed successfully"
        );
    }
}