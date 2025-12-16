package com.example.server.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.dtos.requests.CreateBlockDto;
import com.example.server.entities.BlockEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.BlockRepository;
import com.example.server.repositories.UserRepository;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    public BlockService(BlockRepository blockRepository, UserRepository userRepository) {
        this.blockRepository = blockRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BlockEntity blockUser(String targetUserId, CreateBlockDto dto, UserEntity admin) {
        // 1. Verify if requester is ADMIN
        if (admin.getRole() != RoleEnum.ADMIN) {
            throw new RuntimeException("Access Denied: Only admins can block users.");
        }

        // 2. Fetch target user
        UserEntity targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Optional: Prevent blocking another Admin (Logic safety)
        if (targetUser.getRole() == RoleEnum.ADMIN) {
            throw new RuntimeException("Cannot block another Administrator.");
        }

        // 4. Check if already blocked
        if (blockRepository.existsById(targetUserId)) {
            throw new RuntimeException("User is already blocked. Unblock first to update.");
        }

        // 5. Create Blocking
        BlockEntity blocking = BlockEntity.builder()
                .blockedUser(targetUser) // @MapsId will take the ID from here
                .admin(admin)
                .reason(dto.reason())
                .expirationDate(dto.expirationDate())
                .build();

        return blockRepository.save(blocking);
    }

    @Transactional
    public void unblockUser(String targetUserId, UserEntity admin) {
        if (admin.getRole() != RoleEnum.ADMIN) {
            throw new RuntimeException("Access Denied: Only admins can unblock users.");
        }

        if (!blockRepository.existsById(targetUserId)) {
            throw new RuntimeException("User is not currently blocked.");
        }

        blockRepository.deleteById(targetUserId);
    }
}