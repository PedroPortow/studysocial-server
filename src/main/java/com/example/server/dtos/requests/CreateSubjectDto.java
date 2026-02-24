package com.example.server.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSubjectDto(
    @NotBlank(message = "Name is required")
    @Size(max = 80, message = "Name must be at most 80 characters")
    String name
) {}
