package com.example.server.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentDto(
    @NotBlank(message = "O conteúdo do comentário é obrigatório")
    String content,

    @JsonProperty("parent_id")
    Long parentId
) {}
