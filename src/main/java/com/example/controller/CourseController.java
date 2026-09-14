package com.example.controller;

import com.example.dto.CourseRequestDto;
import com.example.dto.CourseResponseDto;
import com.example.service.CourseService;

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
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(
            summary = "Create a new course",
            description = "Creates a new course and assigns it to a teacher."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
            @ApiResponse(responseCode = "404", description = "Teacher not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(
            @Valid @RequestBody CourseRequestDto dto) {

        return ResponseEntity.ok(
                courseService.createCourse(dto));
    }

    @Operation(
            summary = "Get all courses",
            description = "Returns courses with pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CourseResponseDto>> getAllCourses(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                courseService.getAllCourses(pageable));
    }

    @Operation(
            summary = "Get course by ID",
            description = "Returns a course using the course ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(

            @Parameter(
                    description = "Course ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        return ResponseEntity.ok(
                courseService.getCourseById(id));
    }

    @Operation(
            summary = "Update course",
            description = "Updates an existing course."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(

            @Parameter(
                    description = "Course ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody CourseRequestDto dto) {

        return ResponseEntity.ok(
                courseService.updateCourse(id, dto));
    }

    @Operation(
            summary = "Delete course",
            description = "Deletes a course using the course ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(

            @Parameter(
                    description = "Course ID",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search courses",
            description = "Search courses by name and/or teacher ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CourseResponseDto>> searchCourses(

            @Parameter(
                    description = "Course name to search",
                    example = "Java"
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    description = "Teacher ID",
                    example = "1"
            )
            @RequestParam(required = false) Long teacherId,

            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                courseService.searchCourses(
                        name,
                        teacherId,
                        pageable));
    }
}