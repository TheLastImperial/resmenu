package com.thelastimperial.resmenu.services;

import java.util.Optional;
import java.util.UUID;

import com.thelastimperial.resmenu.controllers.rq.auth.NewPasswordRq;
import com.thelastimperial.resmenu.controllers.rq.auth.NewUserRq;
import com.thelastimperial.resmenu.controllers.rq.auth.RecoveryRq;
import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.entities.UserRecoveryEntity;

public interface AuthService {
    public UserRecoveryEntity setRecovery(RecoveryRq rq);
    public Optional<UserRecoveryEntity> getRecovery(UUID id);
    public UserRecoveryEntity createNewPassword(NewPasswordRq rq);
    public Optional<UserEntity> createUser(NewUserRq rq);
}
