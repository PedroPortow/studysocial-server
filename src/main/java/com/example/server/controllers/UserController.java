package com.example.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  // @PostMapping
  // public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserDto dto) {
  //   UserEntity user = userService.create(dto);

  //   // if (user == null) {
  //   //   // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("deu ruim");
  //   // }

  //   return ResponseEntity.status(HttpStatus.CREATED).body(user);
  // }
}
