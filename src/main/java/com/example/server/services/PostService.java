package com.example.server.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreatePostDto;
import com.example.server.dtos.requests.UpdatePostDto;
import com.example.server.entities.PostEntity;
import com.example.server.entities.UserEntity;
import com.example.server.repositories.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostEntity> findAll() {
        return postRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
    }

    public PostEntity findById(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new RuntimeException("Post não encontrado"));
    }

    public List<PostEntity> findByUser(UserEntity user) {
        return postRepository.findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(user);
    }

    public PostEntity create(CreatePostDto dto, UserEntity user) {
        PostEntity post = PostEntity.builder()
            .title(dto.title())
            .content(dto.content())
            .mediaUrl(dto.mediaUrl())
            .user(user)
            .build();

        return postRepository.save(post);
    }

    public PostEntity update(Long id, UpdatePostDto dto, UserEntity user) {
        PostEntity post = findById(id);

        // Verifica se o usuário é o dono do post
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("Você não tem permissão para editar este post");
        }

        if (dto.title() != null) {
            post.setTitle(dto.title());
        }
        if (dto.content() != null) {
            post.setContent(dto.content());
        }
        if (dto.mediaUrl() != null) {
            post.setMediaUrl(dto.mediaUrl());
        }

        return postRepository.save(post);
    }

    public void delete(Long id, UserEntity user) {
        PostEntity post = findById(id);

        // Verifica se o usuário é o dono do post
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("Você não tem permissão para deletar este post");
        }

        // Soft delete - marca a data de deleção
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
