package com.example.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.server.entities.PostEntity;
import com.example.server.entities.SocietyEntity;
import com.example.server.entities.UserEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    // Buscar posts de um usuário específico
    List<PostEntity> findByUser(UserEntity user);

    // Buscar posts não deletados (soft delete)
    List<PostEntity> findByDeletedAtIsNull();

    // Buscar posts não deletados de um usuário
    List<PostEntity> findByUserAndDeletedAtIsNull(UserEntity user);

    // Buscar posts ordenados por data (mais recentes primeiro)
    List<PostEntity> findByDeletedAtIsNullOrderByCreatedAtDesc();

    // Buscar post por id que não foi deletado
    Optional<PostEntity> findByIdAndDeletedAtIsNull(Long id);

    // Buscar posts de um usuário ordenados por data
    List<PostEntity> findByUserAndDeletedAtIsNullOrderByCreatedAtDesc(UserEntity user);

    // Buscar posts de uma society ordenados por data
    List<PostEntity> findBySocietyAndDeletedAtIsNullOrderByCreatedAtDesc(SocietyEntity society);

    // Buscar posts do feed personalizado: posts globais + posts de grupos que o usuário é membro
    @Query("SELECT p FROM posts p LEFT JOIN p.society s LEFT JOIN s.members m " +
           "WHERE p.deletedAt IS NULL AND (p.society IS NULL OR m = :user) " +
           "ORDER BY p.createdAt DESC")
    List<PostEntity> findByGeneralPostOrSocietyMembership(@Param("user") UserEntity user);
}
