package com.example.server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreateAssessmentDto;
import com.example.server.dtos.requests.UpdateAssessmentDto;
import com.example.server.entities.AssessmentEntity;
import com.example.server.entities.SubjectEntity;
import com.example.server.entities.UserEntity;
import com.example.server.repositories.AssessmentRepository;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final SubjectService subjectService;

    AssessmentService(AssessmentRepository assessmentRepository, SubjectService subjectService) {
        this.assessmentRepository = assessmentRepository;
        this.subjectService = subjectService;
    }

    public List<AssessmentEntity> findBySubject(Long subjectId, UserEntity user) {
        SubjectEntity subject = subjectService.findById(subjectId);
        subjectService.verifyOwnership(subject, user);
        return assessmentRepository.findBySubjectOrderByCreatedAtDesc(subject);
    }

    public AssessmentEntity findById(Long id) {
        return assessmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Assessment not found"));
    }

    public AssessmentEntity create(CreateAssessmentDto dto, UserEntity user) {
        SubjectEntity subject = subjectService.findById(dto.subjectId());
        subjectService.verifyOwnership(subject, user);

        AssessmentEntity assessment = AssessmentEntity.builder()
            .subject(subject)
            .title(dto.title())
            .value(dto.value())
            .weight(dto.weight())
            .build();

        return assessmentRepository.save(assessment);
    }

    public AssessmentEntity update(Long id, UpdateAssessmentDto dto, UserEntity user) {
        AssessmentEntity assessment = findById(id);
        subjectService.verifyOwnership(assessment.getSubject(), user);

        if (dto.title() != null) {
            assessment.setTitle(dto.title());
        }
        if (dto.value() != null) {
            assessment.setValue(dto.value());
        }
        if (dto.weight() != null) {
            assessment.setWeight(dto.weight());
        }

        return assessmentRepository.save(assessment);
    }

    public void delete(Long id, UserEntity user) {
        AssessmentEntity assessment = findById(id);
        subjectService.verifyOwnership(assessment.getSubject(), user);
        assessmentRepository.delete(assessment);
    }
}
