package com.example.server.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.server.dtos.requests.CreateNoteDto;
import com.example.server.dtos.requests.UpdateNoteDto;
import com.example.server.entities.NoteEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.NoteRepository;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteEntity> findByUser(UserEntity user) {
        return noteRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<NoteEntity> findByUserAndSubject(UserEntity user, Long subjectId) {
        return noteRepository.findByUserAndSubjectIdOrderByCreatedAtDesc(user, subjectId);
    }

    public NoteEntity findById(Long id) {
        return noteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anotação não encontrada whooooopss"));
    }

    public NoteEntity create(CreateNoteDto dto, UserEntity user) {
        NoteEntity note = NoteEntity.builder()
            .title(dto.title())
            .content(dto.content())
            .subjectId(dto.subjectId())
            .user(user)
            .build();

        return noteRepository.save(note);
    }

    public NoteEntity update(Long id, UpdateNoteDto dto, UserEntity user) {
        NoteEntity note = findById(id);

        if (!note.getUser().getEmail().equals(user.getEmail()) && user.getRole() != RoleEnum.ADMIN) {
            throw new RuntimeException("no tienes permission :( ");
        }

        if (dto.title() != null) {
            note.setTitle(dto.title());
        }
        if (dto.content() != null) {
            note.setContent(dto.content());
        }

        return noteRepository.save(note);
    }

    public void delete(Long id, UserEntity user) {
        NoteEntity note = findById(id);

        if (!note.getUser().getEmail().equals(user.getEmail()) && user.getRole() != RoleEnum.ADMIN) {
            throw new RuntimeException("no tienes permission :((( ");
        }

        noteRepository.delete(note);
    }
}
