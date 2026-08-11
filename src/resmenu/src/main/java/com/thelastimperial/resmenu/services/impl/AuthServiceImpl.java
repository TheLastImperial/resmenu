package com.thelastimperial.resmenu.services.impl;

import com.thelastimperial.resmenu.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thelastimperial.resmenu.controllers.rq.MailRq;
import com.thelastimperial.resmenu.controllers.rq.auth.NewPasswordRq;
import com.thelastimperial.resmenu.controllers.rq.auth.NewUserRq;
import com.thelastimperial.resmenu.controllers.rq.auth.RecoveryRq;
import com.thelastimperial.resmenu.entities.UserActivationEntity;
import com.thelastimperial.resmenu.entities.UserAuditEntity;
import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.entities.UserRecoveryEntity;
import com.thelastimperial.resmenu.entities.enums.UserAuditAction;
import com.thelastimperial.resmenu.repositories.UserActivationRepository;
import com.thelastimperial.resmenu.repositories.UserAuditRepository;
import com.thelastimperial.resmenu.repositories.UserRecoveryRepository;
import com.thelastimperial.resmenu.services.AuthService;
import com.thelastimperial.resmenu.services.MailRequestService;

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
    private final MailRequestService mailRequestService;
    private final UserActivationRepository userActivationRepository;

    @Override
    public UserRecoveryEntity setRecovery(RecoveryRq rq) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(rq.getUsername());
        UserRecoveryEntity saved = null;
        if(userOpt.isEmpty()){
            handleFailure(rq);
        }else {
            handleSuccess(userOpt.get(), rq);
        }
        return saved;
    }

    @Override
    public Optional<UserRecoveryEntity> getRecovery(String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        }catch(Exception e) {
            log.info("Wrong UUID pattern.");
            return Optional.empty();
        }
        Optional<UserRecoveryEntity> recovery = userRecoveryRepository.findById(uuid);
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
    public Optional<UserEntity> createUser(NewUserRq rq) {
        UserEntity user = UserEntity.builder()
            .email(rq.getUsername())
            .username(rq.getUsername())
            .password(passwordEncoder.encode(rq.getPassword()))
            .enabled(false)
            .build();
        log.info("New User: {}", user);
        UserEntity saved = userRepository.save(user);

        String address = "http://localhost:8080";
        UserActivationEntity activation = userActivationRepository.save(
          UserActivationEntity
          .builder()
          .user(user)
          .build()  
        );
        mailRequestService.sendMail(
            MailRq
            .builder()
            .to(user.getEmail())
            .subject("Account activation")
            .html(true)
            .content("email/activate-account")
            .contentName("ActivateAccount")
            .params(new HashMap<String, Object>() {{
                put("username", user.getUsername());
                put(
                    "url",
                    String.format("%s/auth/activate/%s", address, activation.getId())
                );
            }})
            .build()
        );
        return Optional.of(saved);
    }

    @Override
    public void activateAccount(String tokenId){
        UUID uuid = UUID.randomUUID();
        try{
            uuid = UUID.fromString(tokenId);
        }catch(Exception e){
            log.info("Error casting UUID");
            log.info(e.getMessage());
            return;
        }
        Optional<UserActivationEntity> activationOpt = userActivationRepository.findById(uuid);
        if(activationOpt.isEmpty()){
            log.info("There is not a activation with id: {}", uuid);
            return;
        }
        UserActivationEntity activation = activationOpt.get();
        if(activation.isUsed()){
            log.info("The activation is used.");
            return;
        }
        UserEntity user = activation.getUser();
        user.setEnabled(true);
        activation.setActivatedAt(LocalDateTime.now());
        activation.setUsed(true);;
        userAuditRepository.save(
          UserAuditEntity.builder()
          .action(UserAuditAction.ACCOUNT_ACTIVATION)
          .userId(user.getId())
          .updatedBy(user.getId())
          .build()  
        );
        userRepository.save(user);
        userActivationRepository.save(activation);
    }

    public UserRecoveryEntity handleSuccess(UserEntity user, RecoveryRq rq){
        UserRecoveryEntity toSave = UserRecoveryEntity
            .builder()
            .user(user)
            .email(rq.getUsername())
            .validUntilAt(LocalDateTime.now().plusMinutes(15))
            .isUsed(false)
            .build();
        UserRecoveryEntity saved = userRecoveryRepository.save(toSave);

        String address = "http://localhost:8080";

        mailRequestService.sendMail(
            MailRq
            .builder()
            .to(user.getEmail())
            .subject("Recovery password")
            .html(true)
            .content("email/recovery")
            .contentName("RecoveryPassword")
            .params(new HashMap<String, Object>() {{
                put(
                    "url",
                    String.format("%s/auth/new_password/%s", address, saved.getId())
                );
            }})
            .build()
        );
        return saved;
    }

    public UserRecoveryEntity handleFailure(RecoveryRq rq) {
        log.info("Email doesn't exists: {}", rq.getUsername());
        UserRecoveryEntity toSave = UserRecoveryEntity
            .builder()
            .email(rq.getUsername())
            .validUntilAt(LocalDateTime.now())
            .isUsed(true)
            .build();
        UserRecoveryEntity saved = userRecoveryRepository.save(toSave);
        return saved;        
    }
}
