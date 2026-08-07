package com.thelastimperial.resmenu.services;

import java.util.Optional;

import com.thelastimperial.resmenu.entities.UserEntity;

public interface UserService {
    public Optional<UserEntity> getByUsername(String username);
}
