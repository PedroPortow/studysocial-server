package com.example.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.CommentEntity;
import com.example.server.entities.PostEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
  // buscar comentarios de um post que não foram deletados e não têm parent?
  List<CommentEntity> findByPostAndDeletedAtIsNullAndParentIsNullOrderByCreatedAtAsc(PostEntity post);

  // buscar respostas de um comentario
  List<CommentEntity> findByParentAndDeletedAtIsNullOrderByCreatedAtAsc(CommentEntity parent);

  // buscar todos os comentarios de um post? q loucura esse spring boot
  List<CommentEntity> findByPostAndDeletedAtIsNullOrderByCreatedAtAsc(PostEntity post);

  // contagem dos comentarios
  long countByPostAndDeletedAtIsNull(PostEntity post);
}
