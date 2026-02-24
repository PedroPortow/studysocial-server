package com.example.server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreateSubjectDto;
import com.example.server.dtos.requests.UpdateSubjectDto;
import com.example.server.entities.SubjectEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.SubjectRepository;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<SubjectEntity> findByUser(UserEntity user) {
        return subjectRepository.findByUserOrderByNameAsc(user);
    }

    public SubjectEntity findById(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    public SubjectEntity create(CreateSubjectDto dto, UserEntity user) {
        SubjectEntity subject = SubjectEntity.builder()
            .name(dto.name())
            .user(user)
            .build();

        return subjectRepository.save(subject);
    }

    public SubjectEntity update(Long id, UpdateSubjectDto dto, UserEntity user) {
        SubjectEntity subject = findById(id);
        verifyOwnership(subject, user);

        if (dto.name() != null) {
            subject.setName(dto.name());
        }

        return subjectRepository.save(subject);
    }

    public void delete(Long id, UserEntity user) {
        SubjectEntity subject = findById(id);
        verifyOwnership(subject, user);
        subjectRepository.delete(subject);
    }

    public void verifyOwnership(SubjectEntity subject, UserEntity user) {
        if (!subject.getUser().getEmail().equals(user.getEmail()) && user.getRole() != RoleEnum.ADMIN) {
            throw new RuntimeException("You don't have permission to modify this subject");
        }
    }
}
