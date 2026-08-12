package com.thelastimperial.resdomain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resdomain.entities.UserAuditEntity;

public interface UserAuditRepository extends JpaRepository<UserAuditEntity, UUID>{
    
}
