package com.example.server.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.requests.UpdateBanDto;
import com.example.server.dtos.responses.BanResponseDto;
import com.example.server.entities.BanEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.BanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bans")
@PreAuthorize("hasRole('ADMIN')")
public class BanController {
  private final BanService banService;

  public BanController(BanService banService) {
    this.banService = banService;
  }

  @GetMapping
  public ResponseEntity<List<BanResponseDto>> getAllActiveBans() {
    List<BanEntity> bans = banService.getAllActiveBans();

    List<BanResponseDto> response = bans.stream()
      .map(BanResponseDto::fromEntity)
      .toList();

    return ResponseEntity.ok(response);
  }
  @GetMapping("/{id}")
  public ResponseEntity<BanResponseDto> getBan(@PathVariable Long id) {
    BanEntity ban = banService.getBanById(id);
    return ResponseEntity.ok(BanResponseDto.fromEntity(ban));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BanResponseDto> updateBan(
    @PathVariable Long id,
    @Valid @RequestBody UpdateBanDto dto,
    @AuthenticationPrincipal UserEntity admin
  ) {
    BanEntity updatedBan = banService.updateBan(id, dto, admin);
    return ResponseEntity.ok(BanResponseDto.fromEntity(updatedBan));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> revokeBan(
    @PathVariable Long id,
    @AuthenticationPrincipal UserEntity admin
  ) {
    banService.revokeBan(id, admin, null);
    return ResponseEntity.noContent().build();
  }
}
