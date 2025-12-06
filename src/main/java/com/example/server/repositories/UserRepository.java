package com.example.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
  /* spring já cria findById, save, findAll, bla bla bla... 
    Mas podemos definir métodos personalizados aqui
    Ex: boolean existsByEmail(String email);
  */
}
