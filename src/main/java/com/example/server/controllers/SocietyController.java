package com.example.server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.server.dtos.requests.CreateSocietyDto;
import com.example.server.dtos.requests.UpdateSocietyDto;
import com.example.server.dtos.responses.SocietyResponseDto;
import com.example.server.entities.SocietyEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.SocietyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/society")
public class SocietyController {

    private final SocietyService societyService;

    SocietyController(SocietyService societyService) {
        this.societyService = societyService;
    }

    @PostMapping
    public ResponseEntity<SocietyResponseDto> createGroup(
            @RequestBody @Valid CreateSocietyDto dto,
            @AuthenticationPrincipal UserEntity user
    ) {
        SocietyEntity group = societyService.createGroup(dto, user);
        // Retorna 201 Created com o DTO
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SocietyResponseDto.fromEntity(group, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocietyResponseDto> updateGroup(
            @PathVariable Long id,
            @RequestBody @Valid UpdateSocietyDto dto,
            @AuthenticationPrincipal UserEntity user
    ) {
        // Passamos o user para o service validar se ele é o dono
        SocietyEntity updatedGroup = societyService.updateGroup(id, dto, user);

        return ResponseEntity.ok(SocietyResponseDto.fromEntity(updatedGroup, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user
    ) {
        societyService.deleteGroup(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<SocietyResponseDto> joinGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {
        societyService.joinGroup(id, user);
        SocietyEntity group = societyService.getGroupById(id);
        return ResponseEntity.ok(SocietyResponseDto.fromEntity(group, user));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<SocietyResponseDto> leaveGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {
        societyService.leaveGroup(id, user);
        SocietyEntity group = societyService.getGroupById(id);
        return ResponseEntity.ok(SocietyResponseDto.fromEntity(group, user));
    }

    @GetMapping
    public ResponseEntity<List<SocietyResponseDto>> getAllGroups(
            @AuthenticationPrincipal UserEntity user
    ) {
        List<SocietyEntity> groups = societyService.getAllGroups();

        List<SocietyResponseDto> response = groups.stream()
                .map(group -> SocietyResponseDto.fromEntity(group, user))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<SocietyResponseDto>> getUserGroups(
            @AuthenticationPrincipal UserEntity user
    ) {
        List<SocietyEntity> groups = societyService.getUserGroups(user);

        List<SocietyResponseDto> response = groups.stream()
                .map(group -> SocietyResponseDto.fromEntity(group, user))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocietyResponseDto> getGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user
    ) {
        SocietyEntity group = societyService.getGroupById(id);
        return ResponseEntity.ok(SocietyResponseDto.fromEntity(group, user));
    }
}
