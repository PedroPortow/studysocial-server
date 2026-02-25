package com.example.server.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.PasswordResetTokenEntity;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
  Optional<PasswordResetTokenEntity> findByTokenAndUsedFalse(String token);
}
