package com.example.server.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.example.server.entities.BanEntity;

public class UserBannedException extends AuthenticationException {

  private final BanEntity ban;

  public UserBannedException(BanEntity ban) {
    super("User is banned");
    this.ban = ban;
  }

  public BanEntity getBan() {
    return ban;
  }
}
