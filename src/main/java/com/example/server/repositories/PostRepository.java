package com.example.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.server.entities.PostEntity;
import com.example.server.entities.UserEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // Buscar posts de um usuário específico
    List<PostEntity> findByUser(UserEntity user);
    
    // Buscar posts não deletados
    List<PostEntity> findByIsDeletedFalse();
    
    // Buscar posts não deletados de um usuário
    List<PostEntity> findByUserAndIsDeletedFalse(UserEntity user);
    
    // Buscar posts ordenados por data (mais recentes primeiro)
    List<PostEntity> findByIsDeletedFalseOrderByCreatedAtDesc();
}