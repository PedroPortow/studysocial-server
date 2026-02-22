package com.example.server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.requests.BanUserDto;
import com.example.server.dtos.requests.UpdateUserDto;
import com.example.server.dtos.responses.BanResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.BanEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.BanService;
import com.example.server.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final BanService banService;

  UserController(UserService userService, BanService banService) {
    this.userService = userService;
    this.banService = banService;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponseDto>> findAll(@AuthenticationPrincipal UserEntity user) {
    List<UserEntity> users = userService.findAll(user);
    
    return ResponseEntity.ok(
      users.stream()
        .map(UserResponseDto::fromEntity)
        .toList()
    );
  }

  @PutMapping("/{email}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponseDto> updateUser(
    @PathVariable String email,
    @Valid @RequestBody UpdateUserDto dto,
    @AuthenticationPrincipal UserEntity user
  ) {
    UserEntity updatedUser = userService.updateUser(email, dto, user);
    
    return ResponseEntity.ok(UserResponseDto.fromEntity(updatedUser));
  }

 
  @PostMapping("/{email}/ban")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BanResponseDto> banUser(
    @PathVariable String email,
    @Valid @RequestBody BanUserDto dto,
    @AuthenticationPrincipal UserEntity admin
  ) {
    BanEntity ban = banService.banUser(dto, admin);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(BanResponseDto.fromEntity(ban));
  }

  @DeleteMapping("/{email}/ban")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> unbanUser(
    @PathVariable String email,
    @AuthenticationPrincipal UserEntity admin
  ) {
    banService.unbanUser(email, admin);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{email}/ban")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BanResponseDto> getUserBan(@PathVariable String email) {
    return banService.getActiveBan(email)
      .map(ban -> ResponseEntity.ok(BanResponseDto.fromEntity(ban)))
      .orElse(ResponseEntity.notFound().build());
  }
}
