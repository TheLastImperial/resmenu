package com.thelastimperial.resmenu.services.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thelastimperial.resmenu.controllers.rq.EditProductRq;
import com.thelastimperial.resmenu.controllers.rq.NewProductRq;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.ProductEntity;
import com.thelastimperial.resmenu.entities.SectionEntity;
import com.thelastimperial.resmenu.repositories.ProductRepository;
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
    public List<ProductEntity> getAll(MenuEntity menu, int page, int size) {
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
        ProductEntity product = productRepository.findByIdAndMenu(id, menu)
            .orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST)
            );
        productRepository.delete(product);
    }
}
