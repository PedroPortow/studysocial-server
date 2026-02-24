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

import com.example.server.dtos.requests.CreateAbsenceDto;
import com.example.server.dtos.requests.UpdateAbsenceDto;
import com.example.server.dtos.responses.AbsenceResponseDto;
import com.example.server.entities.AbsenceEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.AbsenceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/absences")
public class AbsenceController {

    private final AbsenceService absenceService;

    AbsenceController(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }

    @GetMapping
    public ResponseEntity<List<AbsenceResponseDto>> findBySubject(
        @RequestParam("subject_id") Long subjectId,
        @AuthenticationPrincipal UserEntity user
    ) {
        List<AbsenceEntity> absences = absenceService.findBySubject(subjectId, user);
        return ResponseEntity.ok(absences.stream().map(AbsenceResponseDto::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbsenceResponseDto> findById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        AbsenceEntity absence = absenceService.findById(id);
        return ResponseEntity.ok(AbsenceResponseDto.fromEntity(absence));
    }

    @PostMapping
    public ResponseEntity<AbsenceResponseDto> create(
        @Valid @RequestBody CreateAbsenceDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        AbsenceEntity absence = absenceService.create(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(AbsenceResponseDto.fromEntity(absence));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbsenceResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateAbsenceDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        AbsenceEntity absence = absenceService.update(id, dto, user);
        return ResponseEntity.ok(AbsenceResponseDto.fromEntity(absence));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        absenceService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
