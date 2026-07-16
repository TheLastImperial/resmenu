package com.thelastimperial.resmenu.services;

import java.util.List;

import com.thelastimperial.resmenu.controllers.rq.EditProductRq;
import com.thelastimperial.resmenu.controllers.rq.NewProductRq;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.ProductEntity;
import com.thelastimperial.resmenu.entities.SectionEntity;

public interface ProductService {
    public ProductEntity create(NewProductRq rq, MenuEntity menu, SectionEntity section);
    public List<ProductEntity> getAll(MenuEntity menu, int page, int size);
    public ProductEntity get(Long id, MenuEntity menu);
    public ProductEntity edit(Long id, EditProductRq rq, MenuEntity menu);
    public void delete(Long id, MenuEntity menu);
}
