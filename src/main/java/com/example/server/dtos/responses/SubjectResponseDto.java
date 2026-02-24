package com.example.server.dtos.responses;

import java.time.LocalDateTime;

import com.example.server.entities.SubjectEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SubjectResponseDto(
    Long id,

    String name,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {
    public static SubjectResponseDto fromEntity(SubjectEntity subject) {
        return new SubjectResponseDto(
            subject.getId(),
            subject.getName(),
            subject.getCreatedAt(),
            subject.getUpdatedAt()
        );
    }
}
