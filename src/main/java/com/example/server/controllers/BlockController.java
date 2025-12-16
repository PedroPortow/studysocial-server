package com.example.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.server.dtos.requests.CreateBlockDto;
import com.example.server.dtos.responses.BlockResponseDto;
import com.example.server.entities.BlockEntity;
import com.example.server.entities.UserEntity;
import com.example.server.services.BlockService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockingService;

    public BlockController(BlockService blockService) {
        this.blockingService = blockService;
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlockResponseDto> blockUser(
            @PathVariable String userId,
            @RequestBody @Valid CreateBlockDto dto,
            @AuthenticationPrincipal UserEntity admin
    ) {
        BlockEntity blocking = blockingService.blockUser(userId, dto, admin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BlockResponseDto.fromEntity(blocking));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> unblockUser(
            @PathVariable String userId,
            @AuthenticationPrincipal UserEntity admin
    ) {
        blockingService.unblockUser(userId, admin);
        return ResponseEntity.noContent().build();
    }
}