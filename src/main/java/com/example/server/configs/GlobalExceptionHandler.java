package com.example.server.configs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.server.dtos.responses.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(ApiResponse.error(401, "Email ou senha inválidos"));
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UsernameNotFoundException ex) {
    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(ApiResponse.error(401, "Usuário não encontrado"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
      .map(error -> error.getField() + ": " + error.getDefaultMessage())
      .findFirst()
      .orElse("Erro de validação");

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiResponse.error(400, message));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.error(500, "Erro interno do servidor"));
  }
}

