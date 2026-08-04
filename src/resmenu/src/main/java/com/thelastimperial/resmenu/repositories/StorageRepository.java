package com.thelastimperial.resmenu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resmenu.entities.StorageEntity;

public interface StorageRepository extends JpaRepository<StorageEntity, UUID>{
}
