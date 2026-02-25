package com.example.server.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDto(
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email
) {}
