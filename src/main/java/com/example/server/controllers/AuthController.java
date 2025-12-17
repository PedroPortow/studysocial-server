package com.example.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.dtos.LoginDto;
import com.example.server.dtos.responses.LoginResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.CourseEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.repositories.CourseRepository;
import com.example.server.repositories.UserRepository;
import com.example.server.services.S3Service;
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

  @Autowired
  private S3Service s3Service;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

    var auth = this.authenticationManager.authenticate(usernamePassword);

    UserEntity user = (UserEntity) auth.getPrincipal();
    var token = tokenService.generateToken(user);

    var loginData = new LoginResponseDto(token, UserResponseDto.fromEntity(user));

    return ResponseEntity.ok(loginData);
  }

  @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserResponseDto> register(
      @RequestParam("full_name") String fullName,
      @RequestParam("email") String email,
      @RequestParam("password") String password,
      @RequestParam("course") String courseName,
      @RequestParam(value = "avatar", required = false) MultipartFile avatar
  ) {
    if (this.repository.findById(email).isPresent()) {
      return ResponseEntity.badRequest().body(null);
    }

    CourseEntity course = this.courseRepository.findById(courseName).orElse(null);
    if (course == null) {
      return ResponseEntity.badRequest().body(null);
    }

    String avatarUrl = null;
    if (avatar != null && !avatar.isEmpty()) {
      avatarUrl = s3Service.uploadFile(avatar, "avatars");
    }

    String hashPassword = new BCryptPasswordEncoder().encode(password);
    
    UserEntity newUser = UserEntity.builder()
      .email(email)
      .password(hashPassword)
      .fullName(fullName)
      .role(RoleEnum.USER)
      .course(course)
      .avatarUrl(avatarUrl)
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
