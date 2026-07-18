package com.thelastimperial.resmenu.services.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thelastimperial.resmenu.controllers.rq.EditSectionRq;
import com.thelastimperial.resmenu.controllers.rq.NewSectionRq;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.SectionEntity;
import com.thelastimperial.resmenu.repositories.SectionRepository;
import com.thelastimperial.resmenu.services.SectionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    @Override
    public SectionEntity create(NewSectionRq rq, MenuEntity menu) {
        SectionEntity toSave = SectionEntity.builder().name(rq.getName()).menu(menu).build();
        return sectionRepository.save(toSave);
    }

    @Override
    public List<SectionEntity> getAll(MenuEntity menu, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return sectionRepository.findAllByMenu(menu, pageable);
    }

    @Override
    public SectionEntity get(Long id, MenuEntity menu) {
        return sectionRepository.findByIdAndMenu(id, menu)
            .orElse(null);
    }

    @Override
    public SectionEntity edit(Long id, EditSectionRq rq, MenuEntity menu) {
        SectionEntity toEdit = sectionRepository.findByIdAndMenu(id, menu).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        BeanUtils.copyProperties(rq, toEdit);
        return sectionRepository.save(toEdit);
    }

    @Override
    public void delete(Long id, MenuEntity menu) {
        SectionEntity section = sectionRepository.findByIdAndMenu(id, menu).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        sectionRepository.delete(section);
    }

    @Override
    public List<SectionEntity> getAll(MenuEntity menuEntity) {
        return sectionRepository.findAllByMenu(menuEntity);
    }
    
}
