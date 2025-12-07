package com.example.server.dtos;

import org.hibernate.validator.constraints.URL;

import com.example.server.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto(
  @NotBlank(message = "nome é obrigatório")
  @Size(min = 3, message = "nome deve ter pelo menos 3 caracteres")
  @JsonProperty("full_name")
  String fullName,

  @NotBlank(message = "email é obrigatório")
  @Email(message = "formato de email inválido")
  String email,

  @NotBlank(message = "senha é obrigatória")
  @Size(min = 6, message = "senha deve ter pelo menos 6 caracteres")
  String password,

  @Nullable
  @URL(message = "formato de url inválido")
  String avatarUrl,
    
  @Nullable
  RoleEnum role
) {}