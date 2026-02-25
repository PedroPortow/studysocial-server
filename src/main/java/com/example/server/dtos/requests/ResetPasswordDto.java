package com.example.server.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
    @NotBlank(message = "Token é obrigatório")
    String token,

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    String newPassword
) {}
