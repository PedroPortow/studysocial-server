package com.example.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.AssessmentEntity;
import com.example.server.entities.SubjectEntity;

@Repository
public interface AssessmentRepository extends JpaRepository<AssessmentEntity, Long> {

    List<AssessmentEntity> findBySubjectOrderByCreatedAtDesc(SubjectEntity subject);
}
