package com.example.server.dtos.requests;

import java.time.LocalDateTime;

import com.example.server.enums.BanType;

public record UpdateBanDto(
  String reason,
  BanType type,
  LocalDateTime expiresAt
) {}
