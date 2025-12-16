package com.example.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.CourseEntity;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, String> {
  List<CourseEntity> findAllByOrderByNameAsc();
}
