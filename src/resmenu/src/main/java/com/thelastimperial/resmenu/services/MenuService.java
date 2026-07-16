package com.thelastimperial.resmenu.services;

import java.util.List;

import com.thelastimperial.resmenu.controllers.rq.EditMenuRq;
import com.thelastimperial.resmenu.controllers.rq.NewMenuRq;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.UserEntity;

public interface MenuService {
    public MenuEntity create(NewMenuRq rq, UserEntity user);
    public MenuEntity get(Long id, UserEntity user);
    public MenuEntity update(EditMenuRq rq, Long id, UserEntity user);
    public void delete(Long id);
    public List<MenuEntity> getAll(UserEntity user, int page, int size);
}
