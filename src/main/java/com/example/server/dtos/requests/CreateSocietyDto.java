package com.example.server.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSocietyDto(
  @NotBlank(message = "Nome é obrigatório") @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres") String name,
  String description
) {}
