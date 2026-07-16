package com.thelastimperial.resmenu.services;

import com.thelastimperial.resmenu.entities.UserEntity;

public interface UserService {
    public UserEntity getByUsername(String username);
}
