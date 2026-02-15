package com.example.server.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.server.entities.SocietyEntity;
import com.example.server.entities.UserEntity;

@Repository
public interface SocietyRepository extends JpaRepository<SocietyEntity, Long> {
    Optional<SocietyEntity> findByName(String name);

    // Find groups where user is a member
    @Query("SELECT g FROM society g JOIN g.members m WHERE m = :user")
    List<SocietyEntity> findByMember(@Param("user") UserEntity user);

    @Query("SELECT COUNT(g) > 0 FROM society g JOIN g.members m WHERE g.id = :societyId AND m.email = :userEmail")
    boolean isMember(@Param("societyId") Long societyId, @Param("userEmail") String userEmail);
}
