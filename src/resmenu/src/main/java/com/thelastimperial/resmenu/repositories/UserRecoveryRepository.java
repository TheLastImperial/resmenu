package com.thelastimperial.resmenu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resmenu.entities.UserRecoveryEntity;

public interface UserRecoveryRepository extends JpaRepository<UserRecoveryEntity, UUID>{
}
