package com.example.server.dtos.responses;

import java.time.LocalDateTime;

import com.example.server.entities.SocietyEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SocietyResponseDto(
        Long id,
        String name,
        String description,

        @JsonProperty("owner")
        UserResponseDto owner,

        @JsonProperty("member_count")
        Integer memberCount,

        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @JsonProperty("updated_at")
        LocalDateTime updatedAt
) {
    public static SocietyResponseDto fromEntity(SocietyEntity entity) {
        if (entity == null) return null;

        return new SocietyResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                UserResponseDto.fromEntity(entity.getOwner()),
                entity.getMembers() != null ? entity.getMembers().size() : 0,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
