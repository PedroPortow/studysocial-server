package com.example.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.server.dtos.responses.BanResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserBannedException.class)
  public ResponseEntity<BanResponseDto> handleUserBanned(UserBannedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(BanResponseDto.fromEntity(ex.getBan()));
  }
}
