package com.example.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.AbsenceEntity;
import com.example.server.entities.SubjectEntity;

@Repository
public interface AbsenceRepository extends JpaRepository<AbsenceEntity, Long> {

    List<AbsenceEntity> findBySubjectOrderByDateDesc(SubjectEntity subject);
}
