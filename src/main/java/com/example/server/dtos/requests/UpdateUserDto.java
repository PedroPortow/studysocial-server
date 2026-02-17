package com.example.server.dtos.requests;

import com.example.server.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserDto(
  @NotBlank(message = "Full name is required")
  @JsonProperty("full_name")
  String fullName,

  @NotNull(message = "Role is required")
  RoleEnum role,

  @JsonProperty("course_name")
  String courseName
) {
}
