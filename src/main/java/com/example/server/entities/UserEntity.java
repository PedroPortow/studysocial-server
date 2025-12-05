package com.example.server.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.server.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data; // gera getters/setters automaticamente 

@Data
@Entity
@Table(name = "users") 
public class UserEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // mudar pra auth

    @Column(name = "full_name", nullable = false) 
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('ADMIN', 'USER') DEFAULT 'USER'") // column definition seta user como default
    private RoleEnum role;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    @CreationTimestamp // gera o created_at automaticamente
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // gera o updated_at automaticamente
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}