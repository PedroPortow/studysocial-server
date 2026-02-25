package com.example.server.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.server.dtos.LoginDto;
import com.example.server.dtos.requests.ForgotPasswordDto;
import com.example.server.dtos.requests.RegisterDto;
import com.example.server.dtos.requests.ResetPasswordDto;
import com.example.server.dtos.responses.LoginResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.CourseEntity;
import com.example.server.entities.PasswordResetTokenEntity;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
import com.example.server.exceptions.UserBannedException;
import com.example.server.repositories.CourseRepository;
import com.example.server.repositories.PasswordResetTokenRepository;
import com.example.server.repositories.UserRepository;

@Service
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final S3Service s3Service;
  private final EmailService emailService;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(
      AuthenticationManager authenticationManager,
      TokenService tokenService,
      S3Service s3Service,
      EmailService emailService,
      UserRepository userRepository,
      CourseRepository courseRepository,
      PasswordResetTokenRepository passwordResetTokenRepository,
      PasswordEncoder passwordEncoder
  ) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
    this.s3Service = s3Service;
    this.emailService = emailService;
    this.userRepository = userRepository;
    this.courseRepository = courseRepository;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
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

  public void forgotPassword(ForgotPasswordDto dto) {
    // Sempre retorna sucesso para não expor se o email existe ou não
    var userOptional = userRepository.findById(dto.email());

    if (userOptional.isEmpty()) {
      logger.warn("Password reset requested for non-existent email: {}", dto.email());
      return;
    }

    String token = UUID.randomUUID().toString();

    PasswordResetTokenEntity resetToken = PasswordResetTokenEntity.builder()
        .email(dto.email())
        .token(token)
        .expiresAt(LocalDateTime.now().plusHours(1))
        .used(false)
        .build();

    passwordResetTokenRepository.save(resetToken);

    emailService.sendPasswordResetEmail(dto.email(), token);
  }

  public void resetPassword(ResetPasswordDto dto) {
    PasswordResetTokenEntity resetToken = passwordResetTokenRepository
        .findByTokenAndUsedFalse(dto.token())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido ou já utilizado"));

    if (resetToken.isExpired()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado. Solicite uma nova redefinição de senha.");
    }

    UserEntity user = userRepository.findById(resetToken.getEmail())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));

    user.setPassword(passwordEncoder.encode(dto.newPassword()));
    userRepository.save(user);

    resetToken.setUsed(true);
    passwordResetTokenRepository.save(resetToken);

    logger.info("Password reset successfully for user: {}", resetToken.getEmail());
  }
}
