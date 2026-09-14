package com.example.controller;

import com.example.dto.ExamRequestDto;
import com.example.dto.ExamResponseDto;
import com.example.service.ExamService;

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
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @Operation(
            summary = "Create exam",
            description = "Creates a new exam for a course."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exam created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid exam data"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ExamResponseDto> createExam(
            @Valid @RequestBody ExamRequestDto dto) {

        return ResponseEntity.ok(
                examService.createExam(dto));
    }

    @Operation(
            summary = "Get all exams",
            description = "Returns all exams with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exams retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<ExamResponseDto>> getAllExams(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                examService.getAllExams(pageable));
    }

    @Operation(
            summary = "Get exam by ID",
            description = "Returns an exam using the exam ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exam found successfully"),
            @ApiResponse(responseCode = "404", description = "Exam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDto> getExamById(

            @Parameter(
                    description = "Exam ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                examService.getExamById(id));
    }

    @Operation(
            summary = "Update exam",
            description = "Updates an existing exam."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exam updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid exam data"),
            @ApiResponse(responseCode = "404", description = "Exam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDto> updateExam(

            @Parameter(
                    description = "Exam ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody ExamRequestDto dto) {

        return ResponseEntity.ok(
                examService.updateExam(id, dto));
    }

    @Operation(
            summary = "Delete exam",
            description = "Deletes an exam."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exam deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Exam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(

            @Parameter(
                    description = "Exam ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        examService.deleteExam(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search exams",
            description = "Search exams by exam name and/or course ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ExamResponseDto>> searchExams(

            @Parameter(
                    description = "Exam name to search",
                    example = "Java Programming"
            )
            @RequestParam(required = false) String examName,

            @Parameter(
                    description = "Course ID",
                    example = "1"
            )
            @RequestParam(required = false) Long courseId,

            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                examService.searchExams(
                        examName,
                        courseId,
                        pageable));
    }
}