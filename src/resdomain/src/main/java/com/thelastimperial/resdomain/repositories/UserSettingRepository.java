package com.thelastimperial.resdomain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resdomain.entities.UserSettingEntity;

public interface UserSettingRepository extends JpaRepository<UserSettingEntity, UUID>{
}
