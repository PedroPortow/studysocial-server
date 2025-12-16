package com.example.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.LoginDto;
import com.example.server.dtos.RegisterDto;
import com.example.server.dtos.responses.LoginResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.CourseEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.CourseRepository;
import com.example.server.repositories.UserRepository;
import com.example.server.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UserRepository repository;

  @Autowired
  private CourseRepository courseRepository;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

    var auth = this.authenticationManager.authenticate(usernamePassword);

    UserEntity user = (UserEntity) auth.getPrincipal();
    var token = tokenService.generateToken(user);

    var loginData = new LoginResponseDto(token, UserResponseDto.fromEntity(user));

    return ResponseEntity.ok(loginData);
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> register(@RequestBody @Valid RegisterDto data) {
    if (this.repository.findById(data.email()).isPresent()) {
      return ResponseEntity.badRequest().body(null);
    }

    CourseEntity course = this.courseRepository.findById(data.course())
        .orElse(null);

    if (course == null) {
      return ResponseEntity.badRequest().body(null);
    }

    String hashPassword = new BCryptPasswordEncoder().encode(data.password());
    
    UserEntity newUser = UserEntity.builder()
      .email(data.email())
      .password(hashPassword)
      .fullName(data.fullName())
      .role(RoleEnum.USER)
      .course(course)
      .avatarUrl(data.avatarUrl())
      .build();

    this.repository.save(newUser);

    return ResponseEntity.ok(UserResponseDto.fromEntity(newUser));
  }


  @GetMapping("/me")
  public ResponseEntity<UserResponseDto> me() {
    var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!(user instanceof UserEntity)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    return ResponseEntity.ok(UserResponseDto.fromEntity((UserEntity) user));
  }
}
