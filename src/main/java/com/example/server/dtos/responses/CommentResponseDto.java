package com.example.server.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;

import com.example.server.entities.CommentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentResponseDto(
  Long id,
  String content,

  @JsonProperty("created_at")
  LocalDateTime createdAt,

  @JsonProperty("updated_at")
  LocalDateTime updatedAt,

  @JsonProperty("parent_id")
  Long parentId,

  UserResponseDto user,

  @JsonProperty("replies")
  List<CommentResponseDto> replies
) {
  public static CommentResponseDto fromEntity(CommentEntity comment) {
    return fromEntity(comment, null);
  }

  public static CommentResponseDto fromEntity(CommentEntity comment, List<CommentResponseDto> replies) {
    return new CommentResponseDto(
      comment.getId(),
      comment.getContent(),
      comment.getCreatedAt(),
      comment.getUpdatedAt(),
      comment.getParent() != null ? comment.getParent().getId() : null,
      UserResponseDto.fromEntity(comment.getUser()),
      replies != null ? replies : List.of()
    );
  }
}
