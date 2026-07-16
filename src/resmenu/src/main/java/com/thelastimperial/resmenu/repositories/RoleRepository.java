package com.thelastimperial.resmenu.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.thelastimperial.resmenu.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, UUID>{
}
