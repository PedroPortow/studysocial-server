package com.example.server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.requests.CreateCourseDto;
import com.example.server.dtos.responses.CourseResponseDto;
import com.example.server.entities.CourseEntity;
import com.example.server.services.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {

  private final CourseService courseService;

  CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @GetMapping
  public ResponseEntity<List<CourseResponseDto>> findAll() {
    List<CourseEntity> courses = courseService.findAll();
    List<CourseResponseDto> response = courses.stream()
        .map(CourseResponseDto::fromEntity)
        .toList();

    return ResponseEntity.ok(response);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CourseResponseDto> create(@Valid @RequestBody CreateCourseDto dto) {
    CourseEntity course = courseService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(CourseResponseDto.fromEntity(course));
  }

  @DeleteMapping("/{name}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable String name) {
    courseService.delete(name);
    return ResponseEntity.noContent().build();
  }
}
