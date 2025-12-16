package com.example.server.dtos.responses;

import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDto(
  String email,

  @JsonProperty("full_name")
  String fullName,

  @JsonProperty("avatar_url")
  String avatarUrl,

  RoleEnum role,

  CourseResponseDto course
) {
  public static UserResponseDto fromEntity(UserEntity user) {
    return new UserResponseDto(
      user.getEmail(),
      user.getFullName(),
      user.getAvatarUrl(),
      user.getRole(),
      CourseResponseDto.fromEntity(user.getCourse())
    );
  }
}

