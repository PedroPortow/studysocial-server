package com.example.server.services;

import org.springframework.stereotype.Service;

import com.example.server.repositories.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // // todo: definir os dtos? 
  // public UserEntity create(CreateUserDto dto) {
  //   UserEntity user = new UserEntity();

  //   /* ... */
  //   userRepository.save(user);

  //   return user;
  // }
 
}