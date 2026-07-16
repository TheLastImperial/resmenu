package com.thelastimperial.resmenu.services;

import java.util.List;

import com.thelastimperial.resmenu.controllers.rq.EditSectionRq;
import com.thelastimperial.resmenu.controllers.rq.NewSectionRq;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.SectionEntity;

public interface SectionService {
    public SectionEntity create(NewSectionRq rq, MenuEntity menu);
    public List<SectionEntity> getAll(MenuEntity menu, int page, int size);
    public SectionEntity get(Long id, MenuEntity menu);
    public SectionEntity edit(Long id, EditSectionRq rq, MenuEntity menu);
    public void delete(Long id, MenuEntity menu);

    public List<SectionEntity> getAll(MenuEntity menuEntity);
}
