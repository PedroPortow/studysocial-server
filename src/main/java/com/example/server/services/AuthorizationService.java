package com.example.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.server.entities.BanEntity;
import com.example.server.entities.UserEntity;
import com.example.server.exceptions.UserBannedException;
import com.example.server.repositories.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Autowired
    BanService banService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Optional<BanEntity> activeBan = banService.getActiveBan(user.getEmail());
        if (activeBan.isPresent() && activeBan.get().isCurrentlyActive()) {
            throw new UserBannedException(activeBan.get());
        }

        return user;
    }
}