package com.thelastimperial.resmenu.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    public List<ProductEntity> findAllByMenu(MenuEntity menu, Pageable pageable);
    public Optional<ProductEntity> findByIdAndMenu(Long id, MenuEntity menu);
}
