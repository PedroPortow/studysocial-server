package com.example.server.dtos.responses;

import java.time.LocalDateTime;
import com.example.server.entities.BlockEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BlockResponseDto(
        @JsonProperty("blocked_user")
        UserResponseDto blockedUser,

        @JsonProperty("admin")
        UserResponseDto admin,

        String reason,

        @JsonProperty("expiration_date")
        LocalDateTime expirationDate,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
    public static BlockResponseDto fromEntity(BlockEntity entity) {
        if (entity == null) return null;

        return new BlockResponseDto(
                UserResponseDto.fromEntity(entity.getBlockedUser()),
                UserResponseDto.fromEntity(entity.getAdmin()),
                entity.getReason(),
                entity.getExpirationDate(),
                entity.getCreatedAt()
        );
    }
}