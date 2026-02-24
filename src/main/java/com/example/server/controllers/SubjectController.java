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
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.requests.CreateSubjectDto;
import com.example.server.dtos.requests.UpdateSubjectDto;
import com.example.server.dtos.responses.SubjectResponseDto;
import com.example.server.entities.SubjectEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.SubjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDto>> findAll(
        @AuthenticationPrincipal UserEntity user
    ) {
        List<SubjectEntity> subjects = subjectService.findByUser(user);
        return ResponseEntity.ok(subjects.stream().map(SubjectResponseDto::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> findById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        SubjectEntity subject = subjectService.findById(id);
        return ResponseEntity.ok(SubjectResponseDto.fromEntity(subject));
    }

    @PostMapping
    public ResponseEntity<SubjectResponseDto> create(
        @Valid @RequestBody CreateSubjectDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        SubjectEntity subject = subjectService.create(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(SubjectResponseDto.fromEntity(subject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateSubjectDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        SubjectEntity subject = subjectService.update(id, dto, user);
        return ResponseEntity.ok(SubjectResponseDto.fromEntity(subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        subjectService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
