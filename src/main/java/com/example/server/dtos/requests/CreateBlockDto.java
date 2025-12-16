package com.example.server.dtos.requests;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBlockDto(
        @NotBlank(message = "Reason is required")
        @Size(max = 255, message = "Reason must be under 255 characters")
        String reason,

        @JsonProperty("expiration_date")
        LocalDateTime expirationDate
) {}