package com.example.server.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.dtos.LoginDto;
import com.example.server.dtos.requests.RegisterDto;
import com.example.server.dtos.responses.LoginResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.CourseEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.exceptions.UserBannedException;
import com.example.server.repositories.CourseRepository;
import com.example.server.repositories.UserRepository;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final S3Service s3Service;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(
      AuthenticationManager authenticationManager,
      TokenService tokenService,
      S3Service s3Service,
      UserRepository userRepository,
      CourseRepository courseRepository,
      PasswordEncoder passwordEncoder
  ) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
    this.s3Service = s3Service;
    this.userRepository = userRepository;
    this.courseRepository = courseRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public LoginResponseDto login(LoginDto loginDto) {
    try {
      var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
      var auth = authenticationManager.authenticate(usernamePassword);

      UserEntity user = (UserEntity) auth.getPrincipal();
      var token = tokenService.generateToken(user);

      return new LoginResponseDto(token, UserResponseDto.fromEntity(user));
    } catch (Exception e) {
      if (e.getCause() instanceof UserBannedException banned) {
        throw banned;
      }
      throw e;
    }
  }

  public UserResponseDto register(RegisterDto registerDto) {
    if (userRepository.findById(registerDto.email()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
    }

    CourseEntity course = courseRepository.findById(registerDto.course())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course not found"));

    String avatarUrl = null;
    if (registerDto.avatar() != null && !registerDto.avatar().isEmpty()) {
      avatarUrl = s3Service.uploadFile(registerDto.avatar(), "avatars");
    }

    String hashPassword = passwordEncoder.encode(registerDto.password());

    UserEntity newUser = UserEntity.builder()
        .email(registerDto.email())
        .password(hashPassword)
        .fullName(registerDto.fullName())
        .role(RoleEnum.USER)
        .course(course)
        .avatarUrl(avatarUrl)
        .build();

    userRepository.save(newUser);

    return UserResponseDto.fromEntity(newUser);
  }

  public UserResponseDto me() {
    var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!(principal instanceof UserEntity user)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
    }

    return UserResponseDto.fromEntity(user);
  }
}
