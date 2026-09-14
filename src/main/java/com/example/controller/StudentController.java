package com.example.controller;

import com.example.dto.StudentRequestDto;
import com.example.dto.StudentResponseDto;
import com.example.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(
            summary = "Create a new student",
            description = "Creates a new student in the education management system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(
            @Valid @RequestBody StudentRequestDto dto) {

        return ResponseEntity.ok(
                studentService.createStudent(dto));
    }

    @Operation(
            summary = "Get all students",
            description = "Returns students using pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                studentService.getAllStudents(pageable));
    }

    @Operation(
            summary = "Search students",
            description = "Search students by name and/or department with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponseDto>> searchStudents(

            @Parameter(
                    description = "Student name to search",
                    example = "Kaviya"
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    description = "Student department to search",
                    example = "Computer Science"
            )
            @RequestParam(required = false) String department,

            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                studentService.searchStudents(
                        name,
                        department,
                        pageable));
    }

    @Operation(
            summary = "Get student by ID",
            description = "Returns a single student using the student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(

            @Parameter(
                    description = "Student ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                studentService.getStudentById(id));
    }

    @Operation(
            summary = "Update student",
            description = "Updates an existing student using the student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(

            @Parameter(
                    description = "Student ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody StudentRequestDto dto) {

        return ResponseEntity.ok(
                studentService.updateStudent(id, dto));
    }

    @Operation(
            summary = "Delete student",
            description = "Deletes a student using the student ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(

            @Parameter(
                    description = "Student ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
}