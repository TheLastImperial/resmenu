package com.thelastimperial.resmenu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resmenu.entities.UserAuditEntity;

public interface UserAuditRepository extends JpaRepository<UserAuditEntity, UUID>{
    
}
