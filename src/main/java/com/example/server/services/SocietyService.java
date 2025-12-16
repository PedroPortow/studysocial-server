package com.example.server.services;

import java.util.List;

import com.example.server.dtos.requests.UpdateSocietyDto;
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

        group.getMembers().add(owner); // O dono é automaticamente um membro

        return societyRepository.save(group);
    }

    public void joinGroup(Long groupId, UserEntity user) {
        SocietyEntity group = getGroupOrThrow(groupId);
        group.getMembers().add(user);
        societyRepository.save(group);
    }

    public void leaveGroup(Long groupId, UserEntity user) {
        SocietyEntity group = getGroupOrThrow(groupId);
        group.getMembers().remove(user);
        societyRepository.save(group);
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

    private SocietyEntity getGroupOrThrow(Long id) {
        return societyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado"));
    }
}
