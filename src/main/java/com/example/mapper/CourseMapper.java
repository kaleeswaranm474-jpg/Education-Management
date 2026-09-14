package com.example.mapper;

import com.example.dto.CourseRequestDto;
import com.example.dto.CourseResponseDto;
import com.example.entity.Course;
import com.example.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequestDto dto, Teacher teacher) {
        Course course = new Course();

        course.setName(dto.getName());
        course.setCourseCode(dto.getCourseCode());
        course.setDescription(dto.getDescription());
        course.setTeacher(teacher);

        return course;
    }

    public CourseResponseDto toResponseDto(Course course) {

        Long teacherId = null;
        String teacherName = null;

        if (course.getTeacher() != null) {
            teacherId = course.getTeacher().getId();
            teacherName = course.getTeacher().getName();
        }

        return new CourseResponseDto(
                course.getId(),
                course.getName(),
                course.getCourseCode(),
                course.getDescription(),
                teacherId,
                teacherName
        );
    }

    public void updateEntity(Course course, CourseRequestDto dto, Teacher teacher) {
        course.setName(dto.getName());
        course.setCourseCode(dto.getCourseCode());
        course.setDescription(dto.getDescription());
        course.setTeacher(teacher);
    }
}