package com.example.server.services;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public User criar(User user) {
        // Validações de negócio aqui
        return userRepository.save(user);
    }
}