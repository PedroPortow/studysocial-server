package com.example.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dtos.LoginDto;
import com.example.server.dtos.RegisterDto;
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
  public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
    System.out.println("CUZINHOOOO 11111");
    var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

    System.out.println("CUZINHOOOO 22222");
    var auth = this.authenticationManager.authenticate(usernamePassword);


    System.out.println("CUZINHOOOO 222255552");
    var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

    System.out.println("CUZINHOOOO 33333");
    return ResponseEntity.ok(token);
  }

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid RegisterDto data) {
    if (this.repository.findById(data.email()).isPresent()){
      return ResponseEntity.badRequest().build();
    }

    String hashPassword = new BCryptPasswordEncoder().encode(data.password());
    
    UserEntity newUser = UserEntity.builder()
      .email(data.email())
      .password(hashPassword)
      .fullName(data.fullName())
      .role(RoleEnum.USER)
      .avatarUrl("https://www.oficinadanet.com.br/imagens/post/65750/justica-decreta-falencia-da-oi-entenda-o-que-acontece-com-a-operadora.jpg")
      .build();

    this.repository.save(newUser);

    return ResponseEntity.ok().build();
  }
}
