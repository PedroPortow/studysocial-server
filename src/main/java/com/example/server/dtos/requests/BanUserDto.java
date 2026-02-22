package com.example.server.dtos.requests;

import java.time.LocalDateTime;

import com.example.server.enums.BanType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BanUserDto(
  @NotBlank(message = "User ID is required")
  String userId,

  String reason,

  @NotNull(message = "Ban type is required")
  BanType type,

  LocalDateTime expiresAt
) {}
