package com.example.server.dtos.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public record RegisterDto(

  @NotBlank(message = "Full name is required")
  String fullName,

  @NotBlank(message = "Email is required")
  String email,

  @NotBlank(message = "Password is required")
  String password,

  @NotBlank(message = "Course is required")
  String course,

  @Nullable
  MultipartFile avatar
) {}
