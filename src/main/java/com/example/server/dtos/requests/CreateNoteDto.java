package com.example.server.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateNoteDto(
    @NotBlank(message = "Titulo é obrigatório")
    @Size(max = 120, message = "Titulo muy grande")
    String title,

    String content,

    @NotNull(message = "ID do assunto é obrigatório")
    @JsonProperty("subject_id")
    Long subjectId
) {}
