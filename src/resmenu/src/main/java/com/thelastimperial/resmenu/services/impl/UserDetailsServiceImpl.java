package com.thelastimperial.resmenu.services.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.services.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOpt = userService.getByUsername(username);
        if(userOpt.isEmpty())
            throw new UsernameNotFoundException(username);

        UserEntity user = userOpt.get();
        Set<GrantedAuthority> authorities = user
            .getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());
        
        User userDetails =  new User(
            user.getId().toString(),
            user.getPassword(),
            user.isEnabled(),
            user.isAccountNonExpired(),
            user.isCredentialsNonExpired(),
            user.isAccountNonLocked(),
            authorities
        );

        log.debug("Debugging UserDetails");
        log.debug("UserDetails: {}", userDetails);
        return userDetails;
    }

}
