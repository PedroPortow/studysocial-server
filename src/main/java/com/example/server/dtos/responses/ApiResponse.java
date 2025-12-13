package com.example.server.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse<T>(
  T data,
  ApiError error
) {
  // response de sucesso
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(data, null);
  }

  // response de erro
  public static <T> ApiResponse<T> error(int status, String message) {
    return new ApiResponse<>(null, new ApiError(status, message));
  }

  public record ApiError(
    int status,
    String message
  ) {}
}

