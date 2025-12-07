package com.example.server.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.server.repositories.UserRepository;
import com.example.server.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // isso aqui é um "bean", pelo que entendi é um objeto gerenciado pelo spring e dá pra usar o autowired pra injetar dependências 
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  TokenService tokenService;
  @Autowired
  UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    var token = this.recoverToken(request);

    if (token != null) {
      var email = tokenService.validateToken(token); // vai retornar a primary key, que é o email :(

      if (!email.isEmpty()) {
        UserDetails user = userRepository.findById(email).orElseThrow(() -> new RuntimeException("user not found")); 

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");

    return authHeader != null ? authHeader.replace("Bearer ", "") : null;
  }
}
