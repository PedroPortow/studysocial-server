package com.example.server.services;

import org.springframework.stereotype.Service;

import com.example.server.dtos.responses.LikeResponseDto;
import com.example.server.entities.LikeEntity;
import com.example.server.entities.LikeId;
import com.example.server.entities.PostEntity;
import com.example.server.entities.UserEntity;
import com.example.server.repositories.LikeRepository;
import com.example.server.repositories.PostRepository;

@Service
public class LikeService {
  private final LikeRepository likeRepository;
  private final PostRepository postRepository;

  LikeService(LikeRepository likeRepository, PostRepository postRepository) {
    this.likeRepository = likeRepository;
    this.postRepository = postRepository;
  }

  public LikeResponseDto toggleLike(Long postId, UserEntity user) {
    PostEntity post = postRepository.findByIdAndDeletedAtIsNull(postId)
        .orElseThrow(() -> new RuntimeException("Post não encontrado"));

    boolean alreadyLiked = likeRepository.existsByUserAndPost(user, post);

    if (alreadyLiked) {
      likeRepository.findByUserAndPost(user, post).ifPresent(likeRepository::delete);
    } else {
        LikeEntity like = LikeEntity.builder()
            .id(new LikeId(user.getEmail(), postId))
            .user(user)
            .post(post)
            .build();
        likeRepository.save(like);
    }

    long likesCount = likeRepository.countByPost(post);
    boolean isLiked = !alreadyLiked; // Inverte porque fizemos toggle

    return new LikeResponseDto(postId, likesCount, isLiked);
  }

  public LikeResponseDto getLikeStatus(Long postId, UserEntity user) {
    PostEntity post = postRepository.findByIdAndDeletedAtIsNull(postId)
        .orElseThrow(() -> new RuntimeException("Post não encontrado"));

    boolean isLiked = likeRepository.existsByUserAndPost(user, post);
    long likesCount = likeRepository.countByPost(post);

    return new LikeResponseDto(postId, likesCount, isLiked);
  }

  public long countLikes(Long postId) {
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post não encontrado"));
    
    return likeRepository.countByPost(post);
  }

  public boolean isLikedByUser(Long postId, UserEntity user) {
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post não encontrado"));
        
    if (post == null) return false;
      
    return likeRepository.existsByUserAndPost(user, post);
  }
}
