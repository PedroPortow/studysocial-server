package com.example.server.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.requests.CreateSocietyDto;
import com.example.server.entities.SocietyEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.SocietyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/society")
public class SocietyController {

    private final SocietyService societyService;

    SocietyController(SocietyService societyService) { this.societyService = societyService; }

    @PostMapping
    public ResponseEntity<SocietyEntity> createGroup(
            @RequestBody @Valid CreateSocietyDto dto,
            @AuthenticationPrincipal UserEntity user
    ) {
        SocietyEntity group = societyService.createGroup(dto, user);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {

        societyService.joinGroup(id, user);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leaveGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {

        societyService.leaveGroup(id, user);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<List<SocietyEntity>> getAllGroups() {
        return ResponseEntity.ok(societyService.getAllGroups());
    }

    @GetMapping("/my")
    public ResponseEntity<List<SocietyEntity>> getUserGroups(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(societyService.getUserGroups(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocietyEntity> getGroup(@PathVariable Long id) {
        return ResponseEntity.ok(societyService.getGroupById(id));
    }
}