package com.example.server.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreateCommentDto;
import com.example.server.dtos.requests.UpdateCommentDto;
import com.example.server.dtos.responses.CommentResponseDto;
import com.example.server.entities.CommentEntity;
import com.example.server.entities.PostEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.CommentRepository;
import com.example.server.repositories.PostRepository;

@Service
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  CommentService(CommentRepository commentRepository, PostRepository postRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
  }

  public CommentResponseDto create(Long postId, CreateCommentDto dto, UserEntity user) {
    PostEntity post = postRepository.findByIdAndDeletedAtIsNull(postId)
        .orElseThrow(() -> new RuntimeException("post não encontrado"));

    CommentEntity parent = null;
    if (dto.parentId() != null) {
      parent = commentRepository
        .findById(dto.parentId())
        .orElseThrow(() -> new RuntimeException("comentário pai não achaadoooo"));
    }

    CommentEntity comment = CommentEntity.builder()
      .content(dto.content())
      .user(user)
      .post(post)
      .parent(parent)
      .build();

    CommentEntity savedComment = commentRepository.save(comment);
    return CommentResponseDto.fromEntity(savedComment);
  }

  public List<CommentResponseDto> findByPost(Long postId) {
    PostEntity post = postRepository.findByIdAndDeletedAtIsNull(postId)
        .orElseThrow(() -> new RuntimeException("post não encontrado"));

    List<CommentEntity> mainComments = commentRepository
        .findByPostAndDeletedAtIsNullAndParentIsNullOrderByCreatedAtAsc(post);

    return mainComments.stream()
        .map(this::buildCommentTree)
        .collect(Collectors.toList());
  }

  // método recursivo pra caçar toda a árvore de comentários, foda-se o desempenho hehe
  private CommentResponseDto buildCommentTree(CommentEntity comment) {
    List<CommentEntity> replies = commentRepository
        .findByParentAndDeletedAtIsNullOrderByCreatedAtAsc(comment);

    List<CommentResponseDto> replyDtos = replies.stream()
        .map(this::buildCommentTree) 
        .collect(Collectors.toList());

    return CommentResponseDto.fromEntity(comment, replyDtos);
  }

  public CommentResponseDto update(Long id, UpdateCommentDto dto, UserEntity user) {
    CommentEntity comment = commentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("comentário não encontrado"));

    if (!comment.getUser().getEmail().equals(user.getEmail())) {
      throw new RuntimeException("não rola, n é admin bobão");
    }

    comment.setContent(dto.content());
    CommentEntity updatedComment = commentRepository.save(comment);

    return CommentResponseDto.fromEntity(updatedComment);
  }

  public void delete(Long id, UserEntity user) {
    CommentEntity comment = commentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("comentário não encontrado"));

    if (!comment.getUser().getEmail().equals(user.getEmail()) && user.getRole() != RoleEnum.ADMIN) {
      throw new RuntimeException("não rola, n é admin bobão");
    }

    comment.setDeletedAt(LocalDateTime.now());
    commentRepository.save(comment);
  }

  public long countByPost(Long postId) {
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("postt não encontrado"));
    
    return commentRepository.countByPostAndDeletedAtIsNull(post);
  }
}
