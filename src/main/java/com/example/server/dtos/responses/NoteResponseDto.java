package com.example.server.dtos.responses;

import java.time.LocalDateTime;

import com.example.server.entities.NoteEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NoteResponseDto(
    Long id,

    String title,

    String content,

    @JsonProperty("subject_id")
    Long subjectId,

    @JsonProperty("subject_name")
    String subjectName,

    @JsonProperty("created_at")
    LocalDateTime createdAt,

    @JsonProperty("updated_at")
    LocalDateTime updatedAt,

    UserResponseDto user
) {
    public static NoteResponseDto fromEntity(NoteEntity note) {
        return new NoteResponseDto(
            note.getId(),
            note.getTitle(),
            note.getContent(),
            note.getSubject().getId(),
            note.getSubject().getName(),
            note.getCreatedAt(),
            note.getUpdatedAt(),
            UserResponseDto.fromEntity(note.getUser())
        );
    }
}
