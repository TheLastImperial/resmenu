package com.thelastimperial.resmenu.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resmenu.controllers.rq.EditMenuRq;
import com.thelastimperial.resmenu.controllers.rq.NewMenuRq;

public interface MenuService {
    public MenuEntity create(NewMenuRq rq, UUID user);
    public MenuEntity get(Long id, UUID user);
    public MenuEntity update(EditMenuRq rq, Long id, UUID user);
    public void delete(Long id);
    public Page<MenuEntity> getAll(UUID user, int page, int size);
}
