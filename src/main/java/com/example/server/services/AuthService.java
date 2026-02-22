package com.example.server.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.server.dtos.LoginDto;
import com.example.server.dtos.responses.LoginResponseDto;
import com.example.server.dtos.responses.UserResponseDto;
import com.example.server.entities.UserEntity;
import com.example.server.exceptions.UserBannedException;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  public AuthService(AuthenticationManager authenticationManager, TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
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
}
