package com.example.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.entities.UserEntity;
import com.example.server.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserDto dto) {
    UserEntity user = userService.create(dto);

    // if (user == null) {
    //   // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("deu ruim");
    // }

    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }
}
