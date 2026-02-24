package com.example.server.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.server.entities.AssessmentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AssessmentResponseDto(
    Long id,

    @JsonProperty("subject_id")
    Long subjectId,

    String title,

    BigDecimal value,

    BigDecimal weight,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {
    public static AssessmentResponseDto fromEntity(AssessmentEntity assessment) {
        return new AssessmentResponseDto(
            assessment.getId(),
            assessment.getSubject().getId(),
            assessment.getTitle(),
            assessment.getValue(),
            assessment.getWeight(),
            assessment.getCreatedAt(),
            assessment.getUpdatedAt()
        );
    }
}
