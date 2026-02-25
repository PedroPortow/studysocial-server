package com.example.server.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.dtos.LoginDto;
import com.example.server.dtos.requests.ForgotPasswordDto;
import com.example.server.dtos.requests.RegisterDto;
import com.example.server.dtos.requests.ResetPasswordDto;
import com.example.server.dtos.responses.LoginResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
    return ResponseEntity.ok(authService.login(loginDto));
  }

  @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponseDto> register(
      @RequestParam("full_name") String fullName,
      @RequestParam("email") String email,
      @RequestParam("password") String password,
      @RequestParam("course") String courseName,
      @RequestParam(value = "avatar", required = false) MultipartFile avatar
  ) {
    var dto = new RegisterDto(fullName, email, password, courseName, avatar);
    return ResponseEntity.ok(authService.register(dto));
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDto> me() {
    return ResponseEntity.ok(authService.me());
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDto dto) {
    authService.forgotPassword(dto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDto dto) {
    authService.resetPassword(dto);
    return ResponseEntity.ok().build();
  }
}
