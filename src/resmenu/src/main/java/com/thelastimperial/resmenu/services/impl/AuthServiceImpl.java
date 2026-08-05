package com.thelastimperial.resmenu.services.impl;

import com.thelastimperial.resmenu.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thelastimperial.resmenu.controllers.rq.auth.NewPasswordRq;
import com.thelastimperial.resmenu.controllers.rq.auth.NewUserRq;
import com.thelastimperial.resmenu.controllers.rq.auth.RecoveryRq;
import com.thelastimperial.resmenu.entities.UserAuditEntity;
import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.entities.UserRecoveryEntity;
import com.thelastimperial.resmenu.entities.enums.UserAuditAction;
import com.thelastimperial.resmenu.repositories.UserAuditRepository;
import com.thelastimperial.resmenu.repositories.UserRecoveryRepository;
import com.thelastimperial.resmenu.services.AuthService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserRecoveryRepository userRecoveryRepository;
    private final UserAuditRepository userAuditRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRecoveryEntity setRecovery(RecoveryRq rq) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(rq.getUsername());
        boolean isUsed = false;
        if(userOpt.isEmpty()){
            log.error("There is traying to recovery password for a user that not exists.");
            isUsed = true;
        }
        UserRecoveryEntity toSave = UserRecoveryEntity
            .builder()
            .user(userOpt.get())
            .validUntilAt(LocalDateTime.now().plusMinutes(15))
            .isUsed(isUsed)
            .build();
        UserRecoveryEntity saved = userRecoveryRepository.save(toSave);
        log.info("URL: localhost:8080/auth/new_password/{}", saved.getId() );
        return saved;
    }

    @Override
    public Optional<UserRecoveryEntity> getRecovery(UUID id) {
        Optional<UserRecoveryEntity> recovery = userRecoveryRepository.findById(id);
        if(recovery.isEmpty()){
            log.info("There is not a recovery id: {}", id);
            return Optional.empty();
        }
        if(recovery.get().isUsed()){
            log.info("The recovery Token is used.");
            return Optional.empty();
        }
        if(recovery.get().getValidUntilAt().isBefore(LocalDateTime.now())){
            log.info("The token is expired.");
            return Optional.empty();
        }
        return recovery;
    }

    @Override
    public UserRecoveryEntity createNewPassword(NewPasswordRq rq) {
        Optional<UserRecoveryEntity> recoveryOpt = getRecovery(rq.getToken());
        if(recoveryOpt.isEmpty())
            return null;
        if(!rq.getPassword().equals(rq.getPasswordConfirmation())){
            log.info("Token: {} try to restart password but doesn't use the same to confirm.");
            return null;
        }
        UserRecoveryEntity recovery = recoveryOpt.get();
        UserEntity user = recovery.getUser();
        recovery.setLastPassword(user.getPassword());
        recovery.setUsed(true);
        user.setPassword(passwordEncoder.encode(rq.getPassword()));
        userRecoveryRepository.save(recovery);
        userRepository.save(user);
        UserAuditEntity audit = UserAuditEntity.builder()
            .userId(user.getId())
            .action(UserAuditAction.RESTART_CREDENTIALS)
            .updatedBy(user.getId())
            .build();
        userAuditRepository.save(audit);
        return recovery;
    }

    @Override
    public boolean createUser(NewUserRq rq) {
        UserEntity user = UserEntity.builder()
            .email(rq.getUsername())
            .username(rq.getUsername())
            .password(passwordEncoder.encode(rq.getPassword()))
            .enabled(false)
            .build();
        log.info("New User: {}", user);
        userRepository.save(user);
        return true;
    }

}
