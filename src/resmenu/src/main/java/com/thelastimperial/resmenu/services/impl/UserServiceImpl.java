package com.thelastimperial.resmenu.services.impl;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.repositories.UserRepository;
import com.thelastimperial.resmenu.services.UserService;

@Service
public class UserServiceImpl implements UserService{
    private final String emailPattern;
    private final UserRepository userRepository;

    public UserServiceImpl(
        @Value("${com.thelastimperial.resmenu.patterns.email}") String emailPattern,
        UserRepository userRepository
    ){
        this.emailPattern = emailPattern;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> getByUsername(String username) {
        Optional<UserEntity> user = Optional.empty();

        if(isUUID(username)){
            user = userRepository
                .findById(UUID.fromString(username));
        }else if(Pattern.compile(emailPattern).matcher(username).matches()){
            user = userRepository
                .findByEmail(username);
        }else{
            user = userRepository
                .findByUsername(username);
        }

        return user;
    }

    public boolean isUUID(String uuid){
        try{
            UUID.fromString(uuid);
            return true;
        }catch(IllegalArgumentException e){
            return false;
        }
    }
    
}
