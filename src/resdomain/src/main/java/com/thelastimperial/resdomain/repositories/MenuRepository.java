package com.thelastimperial.resdomain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.UserEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Long>{
    public List<MenuEntity> findByUser(UserEntity user);
    public Optional<MenuEntity> findByIdAndUser(Long id, UserEntity user);
    public List<MenuEntity> findAllByUser(UserEntity user, Pageable page);
}
