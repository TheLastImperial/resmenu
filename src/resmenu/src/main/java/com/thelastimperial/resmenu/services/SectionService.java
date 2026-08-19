package com.thelastimperial.resmenu.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.SectionEntity;
import com.thelastimperial.resmenu.controllers.rq.EditSectionRq;
import com.thelastimperial.resmenu.controllers.rq.NewSectionRq;

public interface SectionService {
    public SectionEntity create(NewSectionRq rq, MenuEntity menu);
    public Page<SectionEntity> getAll(MenuEntity menu, int page, int size);
    public SectionEntity get(Long id, MenuEntity menu);
    public SectionEntity edit(Long id, EditSectionRq rq, MenuEntity menu);
    public void delete(Long id, MenuEntity menu);

    public List<SectionEntity> getAll(MenuEntity menuEntity);
}
