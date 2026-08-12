package com.thelastimperial.resdomain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.SectionEntity;

public interface SectionRepository extends JpaRepository<SectionEntity, Long>{
    public List<SectionEntity> findAllByMenu(MenuEntity menu, Pageable pageable);
    public Optional<SectionEntity> findByIdAndMenu(Long id, MenuEntity menu);
    public List<SectionEntity> findAllByMenu(MenuEntity menu);
}
