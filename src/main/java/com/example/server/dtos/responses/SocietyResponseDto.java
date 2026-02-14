package com.example.server.dtos.responses;

import java.time.LocalDateTime;

import com.example.server.entities.SocietyEntity;
import com.example.server.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SocietyResponseDto(
        Long id,
        String name,
        String description,

        @JsonProperty("owner")
        UserResponseDto owner,

        @JsonProperty("member_count")
        Integer memberCount,

        @JsonProperty("is_member")
        Boolean isMember,

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
                null, // is_member não disponível sem currentUser
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static SocietyResponseDto fromEntity(SocietyEntity entity, UserEntity currentUser) {
        if (entity == null) return null;

        boolean isMember = false;
        if (currentUser != null && entity.getMembers() != null) {
            isMember = entity.getMembers().stream()
                    .anyMatch(member -> member.getEmail().equals(currentUser.getEmail()));
        }

        return new SocietyResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                UserResponseDto.fromEntity(entity.getOwner()),
                entity.getMembers() != null ? entity.getMembers().size() : 0,
                isMember,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
