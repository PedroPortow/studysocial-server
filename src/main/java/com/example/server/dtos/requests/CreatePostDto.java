package com.example.server.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostDto(
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 180, message = "O título deve ter no máximo 180 caracteres")
    String title,

    String content,

    @JsonProperty("media_url")
    String mediaUrl,

    @JsonProperty("society_id")
    Long societyId
) {}
