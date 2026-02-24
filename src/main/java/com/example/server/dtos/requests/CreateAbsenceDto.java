package com.example.server.dtos.requests;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAbsenceDto(
    @NotNull(message = "Subject ID is required")
    @JsonProperty("subject_id")
    Long subjectId,

    @NotNull(message = "Quantity is required")
    Integer quantity,

    @NotNull(message = "Date is required")
    LocalDate date,

    @Size(max = 80, message = "Reason must be at most 80 characters")
    String reason
) {}
