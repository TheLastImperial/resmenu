package com.thelastimperial.resmenu.services.impl;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.repositories.UserRepository;
import com.thelastimperial.resmenu.services.UserService;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getByUsername(String username) {
        UserEntity user = new UserEntity();
        String emailPatter = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@"+
            "[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";

        if(isUUID(username)){
            user = userRepository
                .findById(UUID.fromString(username))
                .orElseThrow(()-> new UsernameNotFoundException(username));
        }else if(Pattern.compile(emailPatter).matcher(username).matches()){
            user = userRepository
                .findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        }else{
            user = userRepository
                .findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
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
