package com.example.server.dtos.responses;

import java.time.LocalDateTime;

import com.example.server.entities.PostEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PostResponseDto(
    Long id,
    String title,
    String content,

    @JsonProperty("media_url")
    String mediaUrl,

    @JsonProperty("society_id")
    Long societyId,

    @JsonProperty("society_name")
    String societyName,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt,

    UserResponseDto user
) {
    public static PostResponseDto fromEntity(PostEntity post) {
        return new PostResponseDto(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getMediaUrl(),
            post.getSociety() != null ? post.getSociety().getId() : null,
            post.getSociety() != null ? post.getSociety().getName() : null,
            post.getCreatedAt(),
            post.getUpdatedAt(),
            UserResponseDto.fromEntity(post.getUser())
        );
    }
}
