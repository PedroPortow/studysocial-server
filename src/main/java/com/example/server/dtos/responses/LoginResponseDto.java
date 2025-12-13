package com.example.server.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDto(
  String token,

  @JsonProperty("user")
  UserResponseDto user
) {}

