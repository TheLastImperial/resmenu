package com.thelastimperial.resmail.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resmail.entities.MailAuditEntity;

public interface MailAuditRepository extends JpaRepository<MailAuditEntity, UUID>{
}
