package com.example.server.dtos.requests;

import jakarta.validation.constraints.Size;

public record UpdateNoteDto(
    @Size(max = 120, message = "titulo max 120 caracteres")
    String title,

    String content
) {}
