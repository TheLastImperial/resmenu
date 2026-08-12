package com.thelastimperial.resmenu.services.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.ProductEntity;
import com.thelastimperial.resdomain.repositories.MenuRepository;
import com.thelastimperial.resmenu.controllers.rs.ResProductRs;
import com.thelastimperial.resmenu.controllers.rs.ResSectionRs;
import com.thelastimperial.resmenu.controllers.rs.ResmenuRs;
import com.thelastimperial.resmenu.services.ResmenuService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ResmenuServiceImpl implements ResmenuService {
    private final MenuRepository menuRepository;

    @Override
    public ResmenuRs get(Long id) {
        MenuEntity menu = menuRepository.findById(id).orElseThrow(
            ()-> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        ResmenuRs resmenu = new ResmenuRs();
        BeanUtils.copyProperties(menu, resmenu);
        menu.getSections().stream().forEach(sect -> {
            ResSectionRs resSection = new ResSectionRs();
            BeanUtils.copyProperties(sect, resSection);
            List<ProductEntity> products = sect.getProducts();
            products.stream().forEach(prod -> {
                ResProductRs product = new ResProductRs();
                BeanUtils.copyProperties(prod, product);
                resSection.getProducts().add(product);
            });
            resmenu.getSections().add(resSection);
        });
        return resmenu;
    }
    
}
