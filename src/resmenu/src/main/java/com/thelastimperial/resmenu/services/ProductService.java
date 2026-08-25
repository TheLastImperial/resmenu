package com.thelastimperial.resmenu.services;

import org.springframework.data.domain.Page;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.ProductEntity;
import com.thelastimperial.resdomain.entities.SectionEntity;
import com.thelastimperial.resmenu.controllers.rq.EditProductRq;
import com.thelastimperial.resmenu.controllers.rq.NewProductRq;

public interface ProductService {
    public ProductEntity create(NewProductRq rq, MenuEntity menu, SectionEntity section);
    public Page<ProductEntity> getAll(MenuEntity menu, int page, int size);
    public ProductEntity get(Long id, MenuEntity menu);
    public ProductEntity edit(Long id, EditProductRq rq, MenuEntity menu);
    public void delete(Long id, MenuEntity menu);
}
