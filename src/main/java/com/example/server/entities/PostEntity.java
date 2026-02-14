package com.example.server.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "posts")
@Table(name = "posts")
public class PostEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) // carrega o usuário só quand necessário
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user; // spring boot converte automaticamente pra um objeto user

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "society_id", nullable = true)
  private SocietyEntity society;

  @Column(name = "title", nullable = false, length = 180)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column(name = "media_url", nullable = true)
  private String mediaUrl;

  @Column(name = "deleted_at", nullable = true)
  private LocalDateTime deletedAt;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
