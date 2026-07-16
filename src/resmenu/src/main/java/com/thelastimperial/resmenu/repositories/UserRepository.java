package com.thelastimperial.resmenu.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.thelastimperial.resmenu.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, UUID>{
    public Optional<UserEntity> findByEmail(String email);
    public Optional<UserEntity> findByUsername(String username);
}
