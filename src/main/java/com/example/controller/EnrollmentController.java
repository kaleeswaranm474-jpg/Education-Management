package com.example.controller;

import com.example.dto.EnrollmentRequestDto;
import com.example.dto.EnrollmentResponseDto;
import com.example.service.EnrollmentService;

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
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(
            summary = "Create enrollment",
            description = "Enrolls a student into a course."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid enrollment data"),
            @ApiResponse(responseCode = "404", description = "Student or course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EnrollmentResponseDto> createEnrollment(
            @Valid @RequestBody EnrollmentRequestDto dto) {

        return ResponseEntity.ok(
                enrollmentService.createEnrollment(dto));
    }

    @Operation(
            summary = "Get all enrollments",
            description = "Returns all enrollments with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<EnrollmentResponseDto>> getAllEnrollments(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                enrollmentService.getAllEnrollments(pageable));
    }

    @Operation(
            summary = "Get enrollment by ID",
            description = "Returns an enrollment using the enrollment ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollment found successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(

            @Parameter(
                    description = "Enrollment ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                enrollmentService.getEnrollmentById(id));
    }

    @Operation(
            summary = "Update enrollment",
            description = "Updates an existing enrollment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid enrollment data"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> updateEnrollment(

            @Parameter(
                    description = "Enrollment ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody EnrollmentRequestDto dto) {

        return ResponseEntity.ok(
                enrollmentService.updateEnrollment(id, dto));
    }

    @Operation(
            summary = "Delete enrollment",
            description = "Deletes an enrollment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(

            @Parameter(
                    description = "Enrollment ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        enrollmentService.deleteEnrollment(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search enrollments",
            description = "Search enrollments by student ID and/or course ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<EnrollmentResponseDto>> searchEnrollments(

            @Parameter(
                    description = "Student ID",
                    example = "1"
            )
            @RequestParam(required = false) Long studentId,

            @Parameter(
                    description = "Course ID",
                    example = "1"
            )
            @RequestParam(required = false) Long courseId,

            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                enrollmentService.searchEnrollments(
                        studentId,
                        courseId,
                        pageable));
    }
}