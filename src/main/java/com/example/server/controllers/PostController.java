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

import com.example.server.dtos.requests.CreatePostDto;
import com.example.server.dtos.requests.UpdatePostDto;
import com.example.server.dtos.responses.PostResponseDto;
import com.example.server.entities.PostEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> findAll() {
        List<PostEntity> posts = postService.findAll();
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

    @PostMapping
    public ResponseEntity<PostResponseDto> create(
        @Valid @RequestBody CreatePostDto dto,
        @AuthenticationPrincipal UserEntity user
    ) {
        PostEntity post = postService.create(dto, user);
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
