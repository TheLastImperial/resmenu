package com.thelastimperial.resmenu.services.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thelastimperial.resmenu.controllers.rq.EditMenuRq;
import com.thelastimperial.resmenu.controllers.rq.NewMenuRq;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.repositories.MenuRepository;
import com.thelastimperial.resmenu.services.MenuService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MenuServiceImpl implements MenuService{
    private MenuRepository menuRepository;

    @Override
    public MenuEntity create(NewMenuRq rq, UserEntity user) {
        MenuEntity toSave = new MenuEntity();
        BeanUtils.copyProperties(rq, toSave);
        toSave.setUser(user);
        MenuEntity saved = menuRepository.save(toSave);
        return saved;
    }

    @Override
    public MenuEntity get(Long id, UserEntity user) {
        return menuRepository.findByIdAndUser(id, user)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
            );
    }

    @Override
    public MenuEntity update(EditMenuRq rq, Long id, UserEntity user) {
        MenuEntity toUpdate = menuRepository.findByIdAndUser(id, user).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        BeanUtils.copyProperties(rq, toUpdate);
        MenuEntity updated = menuRepository.save(toUpdate);
        return updated;
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    public List<MenuEntity> getAll(UserEntity user, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return menuRepository.findAllByUser(user, pageable);
    }
}
