package com.example.server.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.server.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder // cria objeto sem precisar ficar chamando set toda hora
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Table(name = "users") 
public class UserEntity implements UserDetails {

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

    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;

    // @ManyToOne
    // @JoinColumn(name = "course_id", nullable = false)
    // private CourseEntity course;
    // @Column(name = "course_id", nullable = false)
    // private Long courseId;

    @CreationTimestamp // gera o created_at automaticamente
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // gera o updated_at automaticamente
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      // traduz o RoleEnum para o padrão do spring security
      if (this.role == RoleEnum.ADMIN) {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
      } else {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
      }
    }


    // negócios do spring security
    @Override
    public String getUsername() {
      return email; 
    }

    @Override
    public String getPassword() {
      return password; 
    }

    // negócios do spring security
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}