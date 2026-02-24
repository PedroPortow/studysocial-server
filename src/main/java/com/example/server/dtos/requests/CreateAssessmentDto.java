package com.example.server.dtos.requests;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAssessmentDto(
    @NotNull(message = "Subject ID is required")
    @JsonProperty("subject_id")
    Long subjectId,

    @NotBlank(message = "Title is required")
    @Size(max = 120, message = "Title must be at most 120 characters")
    String title,

    @NotNull(message = "Value is required")
    BigDecimal value,

    @NotNull(message = "Weight is required")
    BigDecimal weight
) {}
