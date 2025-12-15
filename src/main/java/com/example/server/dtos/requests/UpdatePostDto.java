package com.example.server.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Size;

public record UpdatePostDto(
    @Size(max = 180, message = "O título deve ter no máximo 180 caracteres")
    String title,

    String content,

    @JsonProperty("media_url")
    String mediaUrl
) {}
