package com.example.server.dtos.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.server.entities.AbsenceEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AbsenceResponseDto(
    Long id,

    @JsonProperty("subject_id")
    Long subjectId,

    Integer quantity,

    LocalDate date,

    String reason,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {
    public static AbsenceResponseDto fromEntity(AbsenceEntity absence) {
        return new AbsenceResponseDto(
            absence.getId(),
            absence.getSubject().getId(),
            absence.getQuantity(),
            absence.getDate(),
            absence.getReason(),
            absence.getCreatedAt(),
            absence.getUpdatedAt()
        );
    }
}
