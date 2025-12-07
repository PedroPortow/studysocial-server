package com.example.server.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(
  @NotBlank(message = "email é obrigatório")
  @Email(message = "formato de email inválido")
  String email,

  @NotBlank(message = "senha é obrigatória")
  @Size(min = 6, message = "senha deve ter pelo menos 6 caracteres")
  String password
) {}
  