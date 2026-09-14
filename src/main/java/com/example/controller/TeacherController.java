package com.example.controller;

import com.example.dto.TeacherRequestDto;
import com.example.dto.TeacherResponseDto;
import com.example.service.TeacherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(
            summary = "Create a new teacher",
            description = "Creates a new teacher."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid teacher data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TeacherResponseDto> createTeacher(
            @Valid @RequestBody TeacherRequestDto dto) {

        return ResponseEntity.ok(
                teacherService.createTeacher(dto));
    }

    @Operation(
            summary = "Get all teachers",
            description = "Returns teachers with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teachers retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<TeacherResponseDto>> getAllTeachers(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                teacherService.getAllTeachers(pageable));
    }

    @Operation(
            summary = "Get teacher by ID",
            description = "Returns a teacher using the teacher ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher found successfully"),
            @ApiResponse(responseCode = "404", description = "Teacher not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> getTeacherById(

            @Parameter(
                    description = "Teacher ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                teacherService.getTeacherById(id));
    }

    @Operation(
            summary = "Update teacher",
            description = "Updates an existing teacher."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid teacher data"),
            @ApiResponse(responseCode = "404", description = "Teacher not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> updateTeacher(

            @Parameter(
                    description = "Teacher ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody TeacherRequestDto dto) {

        return ResponseEntity.ok(
                teacherService.updateTeacher(id, dto));
    }

    @Operation(
            summary = "Delete teacher",
            description = "Deletes a teacher using the teacher ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Teacher deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Teacher not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(

            @Parameter(
                    description = "Teacher ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        teacherService.deleteTeacher(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search teachers",
            description = "Search teachers by name and/or department."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<TeacherResponseDto>> searchTeachers(

            @Parameter(
                    description = "Teacher name to search",
                    example = "Arun"
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    description = "Teacher department to search",
                    example = "Computer Science"
            )
            @RequestParam(required = false) String department,

            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                teacherService.searchTeachers(
                        name,
                        department,
                        pageable));
    }
}