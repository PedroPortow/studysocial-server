package com.example.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.SubjectEntity;
import com.example.server.entities.UserEntity;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {

    List<SubjectEntity> findByUserOrderByNameAsc(UserEntity user);
}
