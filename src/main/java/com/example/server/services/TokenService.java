package com.example.server.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.server.entities.UserEntity;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(UserEntity user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.create()
        .withIssuer("server-api")
        .withSubject(user.getEmail())
        .withExpiresAt(LocalDateTime.now().plusHours(7).toInstant(ZoneOffset.of("-03:00")))
        .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("deu ruim na geração do token", exception);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.require(algorithm)
        .withIssuer("server-api")
        .build()
        .verify(token)
        .getSubject();
    } catch (JWTVerificationException exception) {
      return ""; 
    }
  }
}
