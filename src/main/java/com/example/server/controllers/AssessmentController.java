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

import com.example.server.dtos.requests.CreateAssessmentDto;
import com.example.server.dtos.requests.UpdateAssessmentDto;
import com.example.server.dtos.responses.AssessmentResponseDto;
import com.example.server.entities.AssessmentEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.AssessmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping
    public ResponseEntity<List<AssessmentResponseDto>> findBySubject(
        @RequestParam("subject_id") Long subjectId,
        @AuthenticationPrincipal UserEntity user
    ) {
        List<AssessmentEntity> assessments = assessmentService.findBySubject(subjectId, user);
        return ResponseEntity.ok(assessments.stream().map(AssessmentResponseDto::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResponseDto> findById(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        AssessmentEntity assessment = assessmentService.findById(id);
        return ResponseEntity.ok(AssessmentResponseDto.fromEntity(assessment));
    }

    @PostMapping
    public ResponseEntity<AssessmentResponseDto> create(
        @Valid @RequestBody CreateAssessmentDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        AssessmentEntity assessment = assessmentService.create(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(AssessmentResponseDto.fromEntity(assessment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssessmentResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateAssessmentDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        AssessmentEntity assessment = assessmentService.update(id, dto, user);
        return ResponseEntity.ok(AssessmentResponseDto.fromEntity(assessment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal UserEntity user
    ) {
        assessmentService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
