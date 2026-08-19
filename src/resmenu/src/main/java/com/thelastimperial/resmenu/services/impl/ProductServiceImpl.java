package com.thelastimperial.resmenu.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.ProductEntity;
import com.thelastimperial.resdomain.entities.SectionEntity;
import com.thelastimperial.resdomain.repositories.ProductRepository;
import com.thelastimperial.resmenu.controllers.rq.EditProductRq;
import com.thelastimperial.resmenu.controllers.rq.NewProductRq;
import com.thelastimperial.resmenu.services.ProductService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Override
    public ProductEntity create(NewProductRq rq, MenuEntity menu, SectionEntity section) {
        ProductEntity toSave = new ProductEntity();
        BeanUtils.copyProperties(rq, toSave);
        toSave.setMenu(menu);
        toSave.setSection(section);
        ProductEntity saved = productRepository.save(toSave);
        return saved;
    }

    @Override
    public Page<ProductEntity> getAll(MenuEntity menu, int page, int size) {
        if(page == 0)
            page = 1;
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findAllByMenu(menu, pageable);
    }

    @Override
    public ProductEntity get(Long id, MenuEntity menu) {
        return productRepository.findByIdAndMenu(id, menu)
            .orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST)
            );
    }

    @Override
    public ProductEntity edit(Long id, EditProductRq rq,  MenuEntity menu) {
        ProductEntity toSave = productRepository.findByIdAndMenu(id, menu).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        BeanUtils.copyProperties(rq, toSave);
        ProductEntity saved = productRepository.save(toSave);
        return saved;
    }

    @Override
    public void delete(Long id, MenuEntity menu) {
        log.info("ProductId: {}, Menu: {}", id, menu.getId());
        ProductEntity product = productRepository.findByIdAndMenu(id, menu)
            .orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST)
            );
        productRepository.delete(product);
    }
}
