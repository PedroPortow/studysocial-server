package com.example.server.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import com.example.server.dtos.requests.UpdatePostDto;
import com.example.server.dtos.responses.PostResponseDto;
import com.example.server.entities.PostEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.PostService;
import com.example.server.services.S3Service;

@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final S3Service s3Service;

  PostController(PostService postService, S3Service s3Service) {
    this.postService = postService;
    this.s3Service = s3Service;
  }

  @GetMapping
  public ResponseEntity<List<PostResponseDto>> findAll(@AuthenticationPrincipal UserEntity user) {
    List<PostEntity> posts = postService.findByGeneralPostOrSocietyMembership(user);
    List<PostResponseDto> response = posts.stream()
        .map(PostResponseDto::fromEntity)
        .toList();

    return ResponseEntity.ok(response);
  }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable Long id) {
      PostEntity post = postService.findById(id);
      return ResponseEntity.ok(PostResponseDto.fromEntity(post));
    }

    @GetMapping("/me")
    public ResponseEntity<List<PostResponseDto>> findMyPosts(@AuthenticationPrincipal UserEntity user) {
      List<PostEntity> posts = postService.findByUser(user);
      List<PostResponseDto> response = posts.stream()
          .map(PostResponseDto::fromEntity)
          .toList();

      return ResponseEntity.ok(response);
    }

    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<PostResponseDto>> findBySociety(@PathVariable Long societyId) {
      List<PostEntity> posts = postService.findBySociety(societyId);
      List<PostResponseDto> response = posts.stream()
          .map(PostResponseDto::fromEntity)
          .toList();

      return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> create(
      @RequestParam("title") String title,
      @RequestParam(value = "content", required = false) String content,
      @RequestParam(value = "media", required = false) MultipartFile media,
      @RequestParam(value = "society_id", required = false) Long societyId,
      @AuthenticationPrincipal UserEntity user
    ) {
      String mediaUrl = null;
      if (media != null && !media.isEmpty()) {
          mediaUrl = s3Service.uploadFile(media, "posts");
      }

      PostEntity post = postService.create(title, content, mediaUrl, user, societyId);
      return ResponseEntity.status(HttpStatus.CREATED).body(PostResponseDto.fromEntity(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> update(
      @PathVariable Long id,
      @Valid @RequestBody UpdatePostDto dto,
      @AuthenticationPrincipal UserEntity user
    ) {
      PostEntity post = postService.update(id, dto, user);
      return ResponseEntity.ok(PostResponseDto.fromEntity(post));
    }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
    @PathVariable Long id,
    @AuthenticationPrincipal UserEntity user
  ) {
    postService.delete(id, user);
    return ResponseEntity.noContent().build();
  }
}
