package com.example.server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreateAbsenceDto;
import com.example.server.dtos.requests.UpdateAbsenceDto;
import com.example.server.entities.AbsenceEntity;
import com.example.server.entities.SubjectEntity;
import com.example.server.entities.UserEntity;
import com.example.server.repositories.AbsenceRepository;

@Service
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final SubjectService subjectService;

    AbsenceService(AbsenceRepository absenceRepository, SubjectService subjectService) {
        this.absenceRepository = absenceRepository;
        this.subjectService = subjectService;
    }

    public List<AbsenceEntity> findBySubject(Long subjectId, UserEntity user) {
        SubjectEntity subject = subjectService.findById(subjectId);
        subjectService.verifyOwnership(subject, user);
        return absenceRepository.findBySubjectOrderByDateDesc(subject);
    }

    public AbsenceEntity findById(Long id) {
        return absenceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Absence not found"));
    }

    public AbsenceEntity create(CreateAbsenceDto dto, UserEntity user) {
        SubjectEntity subject = subjectService.findById(dto.subjectId());
        subjectService.verifyOwnership(subject, user);

        AbsenceEntity absence = AbsenceEntity.builder()
            .subject(subject)
            .quantity(dto.quantity())
            .date(dto.date())
            .reason(dto.reason())
            .build();

        return absenceRepository.save(absence);
    }

    public AbsenceEntity update(Long id, UpdateAbsenceDto dto, UserEntity user) {
        AbsenceEntity absence = findById(id);
        subjectService.verifyOwnership(absence.getSubject(), user);

        if (dto.quantity() != null) {
            absence.setQuantity(dto.quantity());
        }
        if (dto.date() != null) {
            absence.setDate(dto.date());
        }
        if (dto.reason() != null) {
            absence.setReason(dto.reason());
        }

        return absenceRepository.save(absence);
    }

    public void delete(Long id, UserEntity user) {
        AbsenceEntity absence = findById(id);
        subjectService.verifyOwnership(absence.getSubject(), user);
        absenceRepository.delete(absence);
    }
}
