package com.thelastimperial.resmenu.services;

import java.util.Optional;

import com.thelastimperial.resdomain.entities.UserEntity;
import com.thelastimperial.resdomain.entities.UserRecoveryEntity;
import com.thelastimperial.resmenu.controllers.rq.auth.NewPasswordRq;
import com.thelastimperial.resmenu.controllers.rq.auth.NewUserRq;
import com.thelastimperial.resmenu.controllers.rq.auth.RecoveryRq;

public interface AuthService {
    public UserRecoveryEntity setRecovery(RecoveryRq rq);
    public Optional<UserRecoveryEntity> getRecovery(String id);
    public UserRecoveryEntity createNewPassword(NewPasswordRq rq);
    public Optional<UserEntity> createUser(NewUserRq rq);
    public void activateAccount(String tokenId);
}
