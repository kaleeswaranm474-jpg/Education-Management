package com.example.controller;

import com.example.dto.GradeRequestDto;
import com.example.dto.GradeResponseDto;
import com.example.service.GradeService;

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
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @Operation(
            summary = "Create grade",
            description = "Creates a grade for a student examination."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grade created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid grade data"),
            @ApiResponse(responseCode = "404", description = "Enrollment or exam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<GradeResponseDto> createGrade(
            @Valid @RequestBody GradeRequestDto dto) {

        return ResponseEntity.ok(
                gradeService.createGrade(dto));
    }

    @Operation(
            summary = "Get all grades",
            description = "Returns all grades with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grades retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<GradeResponseDto>> getAllGrades(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                gradeService.getAllGrades(pageable));
    }

    @Operation(
            summary = "Get grade by ID",
            description = "Returns a grade using the grade ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grade found successfully"),
            @ApiResponse(responseCode = "404", description = "Grade not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GradeResponseDto> getGradeById(

            @Parameter(
                    description = "Grade ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                gradeService.getGradeById(id));
    }

    @Operation(
            summary = "Update grade",
            description = "Updates an existing grade."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grade updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid grade data"),
            @ApiResponse(responseCode = "404", description = "Grade not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GradeResponseDto> updateGrade(

            @Parameter(
                    description = "Grade ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody GradeRequestDto dto) {

        return ResponseEntity.ok(
                gradeService.updateGrade(id, dto));
    }

    @Operation(
            summary = "Delete grade",
            description = "Deletes a grade."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Grade deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Grade not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(

            @Parameter(
                    description = "Grade ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        gradeService.deleteGrade(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search grades",
            description = "Search grades using enrollment ID, exam ID and/or grade."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<GradeResponseDto>> searchGrades(

            @Parameter(
                    description = "Enrollment ID",
                    example = "1"
            )
            @RequestParam(required = false) Long enrollmentId,

            @Parameter(
                    description = "Exam ID",
                    example = "1"
            )
            @RequestParam(required = false) Long examId,

            @Parameter(
                    description = "Grade value",
                    example = "A"
            )
            @RequestParam(required = false) String grade,

            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                gradeService.searchGrades(
                        enrollmentId,
                        examId,
                        grade,
                        pageable));
    }
}