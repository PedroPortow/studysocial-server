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

import com.example.server.dtos.requests.CreateCommentDto;
import com.example.server.dtos.requests.UpdateCommentDto;
import com.example.server.dtos.responses.CommentResponseDto;
import com.example.server.entities.UserEntity;
import com.example.server.services.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping
  public ResponseEntity<CommentResponseDto> create(
    @PathVariable Long postId,
    @Valid @RequestBody CreateCommentDto dto,
    @AuthenticationPrincipal UserEntity user
  ) {
    CommentResponseDto comment = commentService.create(postId, dto, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(comment);
  }

  @GetMapping
  public ResponseEntity<List<CommentResponseDto>> findByPost(@PathVariable Long postId) {
    List<CommentResponseDto> comments = commentService.findByPost(postId);
    return ResponseEntity.ok(comments);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CommentResponseDto> update(
      @PathVariable Long postId,
      @PathVariable Long id,
      @Valid @RequestBody UpdateCommentDto dto,
      @AuthenticationPrincipal UserEntity user
  ) {
    CommentResponseDto comment = commentService.update(id, dto, user);
    return ResponseEntity.ok(comment);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long postId,
      @PathVariable Long id,
      @AuthenticationPrincipal UserEntity user
  ) {
    commentService.delete(id, user);
    return ResponseEntity.noContent().build();
  }
}
