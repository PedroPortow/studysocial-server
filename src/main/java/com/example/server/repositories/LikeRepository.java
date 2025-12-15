package com.example.server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.LikeEntity;
import com.example.server.entities.LikeId;
import com.example.server.entities.PostEntity;
import com.example.server.entities.UserEntity;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, LikeId> {
  // Verifica se o usuário já curtiu o post
  boolean existsByUserAndPost(UserEntity user, PostEntity post);
    
  // Busca o like pelo usuário e post
  Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
    
  // Conta likes de um post
  long countByPost(PostEntity post);
}
