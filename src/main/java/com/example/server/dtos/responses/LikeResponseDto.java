package com.example.server.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LikeResponseDto(
  @JsonProperty("post_id")
  Long postId,

  @JsonProperty("likes_count")
  long likesCount,

  @JsonProperty("is_liked")
  boolean isLiked
) {}
