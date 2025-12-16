package com.example.server.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentDto(
  @NotBlank(message = "O conteúdo do comentário é obrigatório")
  String content
) {}
