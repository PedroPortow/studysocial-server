package com.example.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.responses.LikeResponseDto;
import com.example.server.entities.UserEntity;
import com.example.server.services.LikeService;

@RestController
@RequestMapping("/posts/{postId}/like")
public class LikeController {
  private final LikeService likeService;

  LikeController(LikeService likeService) {
    this.likeService = likeService;
  }

  @PostMapping
  public ResponseEntity<LikeResponseDto> toggleLike(
    @PathVariable Long postId,
    @AuthenticationPrincipal UserEntity user
  ) {
    LikeResponseDto response = likeService.toggleLike(postId, user);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<LikeResponseDto> getLikeStatus(
    @PathVariable Long postId,
    @AuthenticationPrincipal UserEntity user
  ) {
    LikeResponseDto response = likeService.getLikeStatus(postId, user);
    return ResponseEntity.ok(response);
  }
}
