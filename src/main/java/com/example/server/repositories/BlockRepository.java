package com.example.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.server.entities.BlockEntity;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, String> {}