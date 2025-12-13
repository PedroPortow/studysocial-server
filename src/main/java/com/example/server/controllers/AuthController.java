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
import com.example.server.dtos.responses.ApiResponse;
import com.example.server.dtos.responses.LoginResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.UserEntity;
import com.example.server.enums.RoleEnum;
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

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody @Valid LoginDto loginDto) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

    var auth = this.authenticationManager.authenticate(usernamePassword);

    UserEntity user = (UserEntity) auth.getPrincipal();
    var token = tokenService.generateToken(user);

    var loginData = new LoginResponseDto(token, UserResponseDto.fromEntity(user));

    return ResponseEntity.ok(ApiResponse.success(loginData));
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<UserResponseDto>> register(@RequestBody @Valid RegisterDto data) {
    if (this.repository.findById(data.email()).isPresent()) {
      return ResponseEntity.badRequest()
        .body(ApiResponse.error(400, "Email já cadastrado"));
    }

    String hashPassword = new BCryptPasswordEncoder().encode(data.password());
    
    UserEntity newUser = UserEntity.builder()
      .email(data.email())
      .password(hashPassword)
      .fullName(data.fullName())
      .role(RoleEnum.USER)
      // url aleatório por enquanto
      .avatarUrl("https://www.oficinadanet.com.br/imagens/post/65750/justica-decreta-falencia-da-oi-entenda-o-que-acontece-com-a-operadora.jpg")
      .build();

    this.repository.save(newUser);

    return ResponseEntity.ok(ApiResponse.success(UserResponseDto.fromEntity(newUser)));
  }


  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserResponseDto>> me() {
    var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (!(user instanceof UserEntity)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(401, "Usuário não autenticado"));
    }

    return ResponseEntity.ok(ApiResponse.success(UserResponseDto.fromEntity((UserEntity) user)));
  }
}
