package com.thelastimperial.resdomain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resdomain.entities.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Long>{
    public List<MenuEntity> findByUserId(UUID user);
    public Optional<MenuEntity> findByIdAndUserId(Long id, UUID user);
    public Page<MenuEntity> findAllByUserId(UUID user, Pageable page);
}
