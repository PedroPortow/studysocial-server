package com.example.server.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.dtos.requests.BanUserDto;
import com.example.server.dtos.requests.UpdateBanDto;
import com.example.server.entities.BanEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.BanType;
import com.example.server.repositories.BanRepository;
import com.example.server.repositories.UserRepository;

@Service
public class BanService {

  private final BanRepository banRepository;
  private final UserRepository userRepository;

  public BanService(BanRepository banRepository, UserRepository userRepository) {
    this.banRepository = banRepository;
    this.userRepository = userRepository;
  }

  public BanEntity banUser(BanUserDto dto, UserEntity admin) {
    if (dto.userId().equals(admin.getEmail())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "You cannot ban yourself"
      );
    }

    UserEntity userToBan = userRepository.findById(dto.userId())
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "User not found"
      ));

    Optional<BanEntity> existingBan = getActiveBan(dto.userId());
    if (existingBan.isPresent() && existingBan.get().isCurrentlyActive()) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "User is already banned"
      );
    }

    if (dto.type() == BanType.TEMPORARY && dto.expiresAt() == null) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Temporary ban must have an expiration date"
      );
    }

    if (dto.type() == BanType.PERMANENT && dto.expiresAt() != null) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Permanent ban cannot have an expiration date"
      );
    }

    BanEntity ban = new BanEntity();
    ban.setUserId(dto.userId());
    ban.setUser(userToBan);
    ban.setBannedBy(admin.getEmail());
    ban.setReason(dto.reason());
    ban.setType(dto.type());
    ban.setExpiresAt(dto.expiresAt());
    ban.setIsActive(true);

    return banRepository.save(ban);
  }


  public BanEntity updateBan(Long banId, UpdateBanDto dto, UserEntity admin) {
    BanEntity ban = banRepository.findById(banId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Ban not found"
      ));

    if (!ban.getIsActive()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Cannot update an inactive ban"
      );
    }

    if (dto.reason() != null) {
      ban.setReason(dto.reason());
    }

    if (dto.type() != null) {
      ban.setType(dto.type());
      
      if (dto.type() == BanType.PERMANENT) {
        ban.setExpiresAt(null);
      }
    }

    if (dto.expiresAt() != null) {
      if (ban.getType() == BanType.PERMANENT) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Cannot set expiration date for permanent ban"
        );
      }
      ban.setExpiresAt(dto.expiresAt());
    }

    if (ban.getType() == BanType.TEMPORARY && ban.getExpiresAt() == null) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Temporary ban must have an expiration date"
      );
    }

    return banRepository.save(ban);
  }

  public void revokeBan(Long banId, UserEntity admin, String reason) {
    BanEntity ban = banRepository.findById(banId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Ban not found"
      ));

    if (!ban.getIsActive()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Ban is already inactive"
      );
    }

    ban.setIsActive(false);
    ban.setRevokedAt(LocalDateTime.now());
    ban.setRevokedBy(admin.getEmail());
    ban.setRevokeReason(reason);

    banRepository.save(ban);
  }

  public void unbanUser(String userId, UserEntity admin) {
    BanEntity ban = banRepository.findByUserIdAndIsActiveTrue(userId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "No active ban found for this user"
      ));

    ban.setIsActive(false);
    ban.setRevokedAt(LocalDateTime.now());
    ban.setRevokedBy(admin.getEmail());

    banRepository.save(ban);
  }

  public Optional<BanEntity> getActiveBan(String userId) {
    return banRepository.findByUserIdAndIsActiveTrue(userId)
      .filter(BanEntity::isCurrentlyActive);
  }

  public List<BanEntity> getAllActiveBans() {
    return banRepository.findByIsActiveTrueOrderByCreatedAtDesc()
      .stream()
      .filter(BanEntity::isCurrentlyActive)
      .toList();
  }

  public BanEntity getBanById(Long banId) {
    return banRepository.findById(banId)
      .orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Ban not found"
      ));
  }

  public boolean isUserBanned(String userId) {
    return getActiveBan(userId).isPresent();
  }
}
