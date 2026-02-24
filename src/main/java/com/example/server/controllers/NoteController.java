package com.example.server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.requests.CreateNoteDto;
import com.example.server.dtos.requests.UpdateNoteDto;
import com.example.server.dtos.responses.NoteResponseDto;
import com.example.server.entities.NoteEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.NoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> findAll(
        @RequestParam(value = "subject_id", required = false) Long subjectId,
        @AuthenticationPrincipal UserEntity user
    ) {
        List<NoteEntity> notes;

        if (subjectId != null) {
            notes = noteService.findByUserAndSubject(user, subjectId);
        } else {
            notes = noteService.findByUser(user);
        }

        return ResponseEntity.ok(notes.stream().map(NoteResponseDto::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> findById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        NoteEntity note = noteService.findById(id);
        return ResponseEntity.ok(NoteResponseDto.fromEntity(note));
    }

    @PostMapping
    public ResponseEntity<NoteResponseDto> create(
        @Valid @RequestBody CreateNoteDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        NoteEntity note = noteService.create(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(NoteResponseDto.fromEntity(note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateNoteDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        NoteEntity note = noteService.update(id, dto, user);
        return ResponseEntity.ok(NoteResponseDto.fromEntity(note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        noteService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
