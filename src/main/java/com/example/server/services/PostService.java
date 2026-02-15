package com.example.server.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.UpdatePostDto;
import com.example.server.entities.PostEntity;
import com.example.server.entities.SocietyEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.PostRepository;
import com.example.server.repositories.SocietyRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final SocietyRepository societyRepository;

    PostService(PostRepository postRepository, SocietyRepository societyRepository) {
        this.postRepository = postRepository;
        this.societyRepository = societyRepository;
    }

    public List<PostEntity> findAll() {
        return postRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
    }

    public List<PostEntity> findByGeneralPostOrSocietyMembership(UserEntity user) {
        return postRepository.findByGeneralPostOrSocietyMembership(user);
    }

    public PostEntity findById(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new RuntimeException("Post não encontrado"));
    }

    public List<PostEntity> findByUser(UserEntity user) {
        return postRepository.findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(user);
    }

    public List<PostEntity> findBySociety(Long societyId) {
        SocietyEntity society = societyRepository.findById(societyId)
            .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
        return postRepository.findBySocietyAndDeletedAtIsNullOrderByCreatedAtDesc(society);
    }

    public PostEntity create(String title, String content, String mediaUrl, UserEntity user, Long societyId) {
        SocietyEntity society = null;

        if (societyId != null) {
            society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
                
            boolean isMember = societyRepository.isMember(societyId, user.getEmail());
            boolean isOwner = society.getOwner().getEmail().equals(user.getEmail());

            if (!isMember && !isOwner) {
                throw new RuntimeException("Você precisa ser membro do grupo para postar nele");
            }
        }

        PostEntity post = PostEntity.builder()
            .title(title)
            .content(content)
            .mediaUrl(mediaUrl)
            .user(user)
            .society(society)
            .build();

        return postRepository.save(post);
    }

    public PostEntity update(Long id, UpdatePostDto dto, UserEntity user) {
        PostEntity post = findById(id);

        // Verifica se o usuário é o dono do post
        if (!post.getUser().getEmail().equals(user.getEmail()) && user.getRole() != RoleEnum.ADMIN) {
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
        if (!post.getUser().getEmail().equals(user.getEmail()) && user.getRole() != RoleEnum.ADMIN) {
            throw new RuntimeException("Você não tem permissão para deletar este post");
        }

        // Soft delete - marca a data de deleção
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
