package com.example.server.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCourseDto(
  @NotBlank(message = "O nome do curso é obrigatório")
  @Size(min = 3, max = 100, message = "O nome do curso deve ter entre 3 e 100 caracteres")
  String name
) {}
