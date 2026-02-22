package com.example.server.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.server.enums.BanType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_bans")
@Table(name = "user_bans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BanEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private UserEntity user;

  @Column(name = "banned_by", nullable = false)
  private String bannedBy;

  @Column(name = "reason", length = 1000)
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, columnDefinition = "ENUM('TEMPORARY', 'PERMANENT')")
  private BanType type;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
  private Boolean isActive = true;

  @Column(name = "revoked_at")
  private LocalDateTime revokedAt;

  @Column(name = "revoked_by")
  private String revokedBy;

  @Column(name = "revoke_reason", length = 500)
  private String revokeReason;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public boolean isCurrentlyActive() {
    if (!isActive) {
      return false;
    }

    if (type == BanType.PERMANENT) {
      return true;
    }

    return expiresAt != null && expiresAt.isAfter(LocalDateTime.now());
  }
}
