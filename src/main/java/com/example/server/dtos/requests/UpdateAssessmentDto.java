package com.example.server.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.Size;

public record UpdateAssessmentDto(
    @Size(max = 120, message = "Title must be at most 120 characters")
    String title,

    BigDecimal value,

    BigDecimal weight
) {}
