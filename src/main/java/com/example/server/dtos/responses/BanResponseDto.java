package com.example.server.dtos.responses;

import java.time.LocalDateTime;

import com.example.server.entities.BanEntity;
import com.example.server.enums.BanType;

public record BanResponseDto(
  Long id,
  String userId,
  UserResponseDto user,
  String bannedBy,
  String reason,
  BanType type,
  LocalDateTime expiresAt,
  Boolean isActive,
  LocalDateTime createdAt,
  LocalDateTime updatedAt,
  LocalDateTime revokedAt,
  String revokedBy
) {
  public static BanResponseDto fromEntity(BanEntity ban) {
    return new BanResponseDto(
      ban.getId(),
      ban.getUserId(),
      ban.getUser() != null ? UserResponseDto.fromEntity(ban.getUser()) : null,
      ban.getBannedBy(),
      ban.getReason(),
      ban.getType(),
      ban.getExpiresAt(),
      ban.getIsActive(),
      ban.getCreatedAt(),
      ban.getUpdatedAt(),
      ban.getRevokedAt(),
      ban.getRevokedBy()
    );
  }
}
