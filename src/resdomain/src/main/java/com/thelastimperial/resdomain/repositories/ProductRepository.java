package com.thelastimperial.resdomain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    public Page<ProductEntity> findAllByMenu(MenuEntity menu, Pageable pageable);
    public Optional<ProductEntity> findByIdAndMenu(Long id, MenuEntity menu);
}
