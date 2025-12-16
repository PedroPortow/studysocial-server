package com.example.server.dtos.responses;

import com.example.server.entities.CourseEntity;

public record CourseResponseDto(
  String name
) {
  public static CourseResponseDto fromEntity(CourseEntity course) {
    if (course == null) return null;
    return new CourseResponseDto(course.getName());
  }
}
