package com.thelastimperial.resdomain.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.thelastimperial.resdomain.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, UUID>{
}
