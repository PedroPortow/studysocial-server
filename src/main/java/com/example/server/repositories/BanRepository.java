package com.example.server.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.BanEntity;

@Repository
public interface BanRepository extends JpaRepository<BanEntity, Long> {
  Optional<BanEntity> findByUserIdAndIsActiveTrue(String userId);

  List<BanEntity> findByIsActiveTrueOrderByCreatedAtDesc();

  List<BanEntity> findByUserIdOrderByCreatedAtDesc(String userId);
}
