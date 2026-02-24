package com.example.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.NoteEntity;
import com.example.server.entities.UserEntity;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    List<NoteEntity> findByUserOrderByCreatedAtDesc(UserEntity user);

    List<NoteEntity> findByUserAndSubjectIdOrderByCreatedAtDesc(UserEntity user, Long subjectId);
}
