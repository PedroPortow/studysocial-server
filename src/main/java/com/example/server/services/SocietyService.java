package com.example.server.services;

import java.util.List;

import com.example.server.dtos.requests.UpdateSocietyDto;
import com.example.server.enums.RoleEnum;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreateSocietyDto;
import com.example.server.entities.SocietyEntity;
import com.example.server.entities.UserEntity;
import com.example.server.repositories.SocietyRepository;
import com.example.server.repositories.UserRepository;

@Service
public class SocietyService {
    private final SocietyRepository societyRepository;
    private final UserRepository userRepository;

    public SocietyService(SocietyRepository societyRepository, UserRepository userRepository) { this.societyRepository = societyRepository; this.userRepository = userRepository; }

    public SocietyEntity createGroup(CreateSocietyDto dto, UserEntity owner) {
        if (societyRepository.findByName(dto.name()).isPresent()) {
            throw new RuntimeException("Grupo com este nome já existe");
        }

        SocietyEntity group = SocietyEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .owner(owner)
                .build();

        group.getMembers().add(owner);

        return societyRepository.save(group);
    }

    @Transactional
    public void joinGroup(Long groupId, UserEntity user) {
        SocietyEntity group = getGroupOrThrow(groupId);

        boolean isAlreadyMember = group.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(user.getEmail()));

        if (isAlreadyMember) {
            throw new RuntimeException("Você já é membro deste grupo");
        }

        group.getMembers().add(user);
        societyRepository.save(group);
    }

    @Transactional
    public void leaveGroup(Long groupId, UserEntity user) {
        SocietyEntity group = getGroupOrThrow(groupId);

        if (group.getOwner().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("O dono do grupo não pode sair do próprio grupo");
        }

        boolean isMember = group.getMembers().stream()
                .anyMatch(member -> member.getEmail().equals(user.getEmail()));

        if (!isMember) {
            throw new RuntimeException("Você não é membro deste grupo");
        }

        boolean removed = group.getMembers().removeIf(member -> member.getEmail().equals(user.getEmail()));

        if (removed) {
            societyRepository.save(group);
        }
    }

    public List<SocietyEntity> getAllGroups() {
        return societyRepository.findAll();
    }

    public List<SocietyEntity> getUserGroups(UserEntity user) {
        return societyRepository.findByMember(user);
    }

    public SocietyEntity getGroupById(Long id) {
        return getGroupOrThrow(id);
    }

    @Transactional
    public SocietyEntity updateGroup(Long societyId, UpdateSocietyDto dto, UserEntity currentUser) {
        SocietyEntity society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));

        if (!society.getOwner().getEmail().equals(currentUser.getEmail())) {
            throw new RuntimeException("Permissão negada. Apenas o dono pode editar o grupo.");
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            society.setName(dto.name());
        }

        if (dto.description() != null) {
            society.setDescription(dto.description());
        }

        return societyRepository.save(society);
    }

    @Transactional
    public void deleteGroup(Long societyId, UserEntity currentUser) {
        SocietyEntity society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));

        boolean isOwner = society.getOwner().getEmail().equals(currentUser.getEmail());
        boolean isAdmin = currentUser.getRole() == RoleEnum.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("Permissão negada. Apenas o dono ou administradores podem excluir o grupo.");
        }

        societyRepository.delete(society);
    }

    private SocietyEntity getGroupOrThrow(Long id) {
        return societyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
    }
}
