package com.example.server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreateCourseDto;
import com.example.server.entities.CourseEntity;
import com.example.server.repositories.CourseRepository;

@Service
public class CourseService {
  private final CourseRepository courseRepository;

  CourseService(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  public List<CourseEntity> findAll() {
    return courseRepository.findAllByOrderByNameAsc();
  }

  public CourseEntity findByName(String name) {
    return courseRepository.findById(name)
        .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
  }

  public CourseEntity create(CreateCourseDto dto) {
    if (courseRepository.existsById(dto.name())) {
      throw new RuntimeException("Curso já existe");
    }

    CourseEntity course = CourseEntity.builder()
        .name(dto.name())
        .build();

    return courseRepository.save(course);
  }

  public void delete(String name) {
    if (!courseRepository.existsById(name)) {
      throw new RuntimeException("Curso não encontrado");
    }
    courseRepository.deleteById(name);
  }
}
