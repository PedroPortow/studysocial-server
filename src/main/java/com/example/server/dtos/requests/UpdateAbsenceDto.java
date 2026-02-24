package com.example.server.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

public record UpdateAbsenceDto(
    Integer quantity,

    LocalDate date,

    @Size(max = 80, message = "Reason must be at most 80 characters")
    String reason
) {}
