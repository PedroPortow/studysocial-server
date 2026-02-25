package com.example.server.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "password_reset_tokens")
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "used", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private Boolean used = false;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public boolean isExpired() {
    return expiresAt.isBefore(LocalDateTime.now());
  }

  public boolean isValid() {
    return !used && !isExpired();
  }
}
