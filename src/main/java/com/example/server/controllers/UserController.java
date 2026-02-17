package com.example.server.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.requests.UpdateUserDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.UserEntity;
import com.example.server.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponseDto>> findAll(@AuthenticationPrincipal UserEntity user) {
    List<UserEntity> users = userService.findAll(user);
    
    return ResponseEntity.ok(
      users.stream()
        .map(UserResponseDto::fromEntity)
        .toList()
    );
  }

  @PutMapping("/{email}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponseDto> updateUser(
    @PathVariable String email,
    @Valid @RequestBody UpdateUserDto dto,
    @AuthenticationPrincipal UserEntity user
  ) {
    UserEntity updatedUser = userService.updateUser(email, dto, user);
    
    return ResponseEntity.ok(UserResponseDto.fromEntity(updatedUser));
  }
}
