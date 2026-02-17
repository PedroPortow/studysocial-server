package com.example.server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.UpdateUserDto;
import com.example.server.entities.CourseEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.CourseRepository;
import com.example.server.repositories.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;

  UserService(UserRepository userRepository, CourseRepository courseRepository) {
    this.userRepository = userRepository;
    this.courseRepository = courseRepository;
  }

  public List<UserEntity> findAll(UserEntity currentUser) {
    // Admin verification (defense in depth)
    if (currentUser.getRole() != RoleEnum.ADMIN) {
      throw new RuntimeException("Acesso negado: apenas administradores podem listar usuários");
    }

    return userRepository.findAll();
  }

  public UserEntity updateUser(String email, UpdateUserDto dto, UserEntity currentUser) {
    if (currentUser.getRole() != RoleEnum.ADMIN) {
      throw new RuntimeException("Acesso negado: apenas administradores podem editar usuários");
    }

    UserEntity user = userRepository.findById(email)
      .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (user.getEmail().equals(currentUser.getEmail()) &&
        dto.role() == RoleEnum.USER &&
        currentUser.getRole() == RoleEnum.ADMIN) {
      throw new RuntimeException("Você não pode remover seu próprio privilégio de administrador");
    }

    if (dto.fullName() != null && !dto.fullName().isBlank()) {
      user.setFullName(dto.fullName());
    }

    if (dto.role() != null) {
      user.setRole(dto.role());
    }

    if (dto.courseName() != null && !dto.courseName().isBlank()) {
      CourseEntity course = courseRepository.findById(dto.courseName())
        .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
      user.setCourse(course);
    }

    return userRepository.save(user);
  }
}
