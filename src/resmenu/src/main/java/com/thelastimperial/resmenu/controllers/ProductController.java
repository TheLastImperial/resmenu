package com.thelastimperial.resmenu.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.entities.ProductEntity;
import com.thelastimperial.resdomain.entities.SectionEntity;
import com.thelastimperial.resdomain.entities.StorageEntity;
import com.thelastimperial.resdomain.entities.UserEntity;
import com.thelastimperial.resmenu.controllers.rq.EditProductRq;
import com.thelastimperial.resmenu.controllers.rq.NewProductRq;
import com.thelastimperial.resmenu.controllers.rs.MenuRs;
import com.thelastimperial.resmenu.controllers.rs.ProductRs;
import com.thelastimperial.resmenu.controllers.rs.SectionRs;
import com.thelastimperial.resmenu.services.MenuService;
import com.thelastimperial.resmenu.services.ProductService;
import com.thelastimperial.resmenu.services.SectionService;
import com.thelastimperial.resmenu.services.StorageService;
import com.thelastimperial.resmenu.services.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/products")
@Slf4j
public class ProductController {
    private final UserService userService;
    private final ProductService productService;
    private final MenuService menuService;
    private final SectionService sectionService;
    private final StorageService storageService;

    @GetMapping("/show/{menuId}/{id}")
    public String get(@PathVariable Long menuId, @PathVariable Long id, Principal principal,
        Model model
    ) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user.orElse(null));
        ProductEntity product = productService.get(id, menu);
        ProductRs response = new ProductRs();
        BeanUtils.copyProperties(product, response);

        model.addAttribute("product", response);
        model.addAttribute("menuId", menuId);
        return "/products/show";
    }
    
    @GetMapping("/menu/{menuId}")
    public String index(Principal principal, Model model, @PathVariable Long menuId,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size
    ) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user.orElse(null));
        Page<ProductEntity> pageData = productService.getAll(menu, page, size);
        List<ProductRs> products = pageData.stream().map(prod -> {
            ProductRs resp = new ProductRs();
            BeanUtils.copyProperties(prod, resp);
            return resp;
        }).collect(Collectors.toList());

        MenuRs menuRs = new MenuRs();
        BeanUtils.copyProperties(menu, menuRs);
        model.addAttribute("products", products);
        model.addAttribute("menuId", menuId);
        model.addAttribute("menu", menuRs);

        model.addAttribute("pageSize", size);
        model.addAttribute("totalElements", pageData.getTotalElements());
        model.addAttribute("currentPage", pageData.getNumber());
        model.addAttribute("totalPages", pageData.getTotalPages());

        return "/products/index";
    }
    
    @GetMapping("/new/{menuId}")
    public String newProduct(
        @PathVariable Long menuId, NewProductRq newProductRq, Principal principal, Model model
    ) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user.orElse(null));        
        List<SectionRs> sections = sectionService.getAll(menu).stream().map(sect -> {
            SectionRs section = new SectionRs();
            BeanUtils.copyProperties(sect, section);
            return section;
        }).collect(Collectors.toList());

        newProductRq.setMenuId(menuId);
        model.addAttribute("sections", sections);
        return "products/new";
    }
    
    @PostMapping("/create")
    public String create(
        NewProductRq newProductRq, BindingResult result, Principal principal
    ) throws Exception{
        log.info("Create Product Request: {}", newProductRq);
        if(result.hasErrors()){
            log.error("Errors: {}", result);
            return "redirect:/products/new/" + newProductRq.getMenuId();
        }

        if(newProductRq.getImage() != null){
            StorageEntity storageEntity = storageService.create(newProductRq.getImage());
            newProductRq.setImageId(storageEntity.getId());
        }

        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(newProductRq.getMenuId(), user.orElse(null));
        SectionEntity section = sectionService.get(newProductRq.getSectionId(), menu);
        ProductEntity product = productService.create(newProductRq, menu, section);
        return "redirect:/products/show/" + menu.getId() + "/" + product.getId();
    }

    @GetMapping("/delete/{menuId}/{id}")
    public String delete(@PathVariable Long menuId, @PathVariable Long id, Principal principal) {
        log.info("MenuId: {}, ProductId: {}, Principal: {}", menuId, id, principal);
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user.orElse(null));
        productService.delete(id, menu);
        return "redirect:/products/menu/" + menuId;
    }

    @GetMapping("/edit/{menuId}/{id}")
    public String edit(@PathVariable Long menuId, @PathVariable Long id, 
        EditProductRq editProductRq, Model model, Principal principal
    ) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user.orElse(null));
        ProductEntity product = productService.get(id, menu);
        List<SectionRs> sections = sectionService.getAll(menu).stream().map(sect -> {
            SectionRs section = new SectionRs();
            BeanUtils.copyProperties(sect, section);
            return section;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(product, editProductRq);
        editProductRq.setSectionId(product.getSection().getId());
        log.info("Rq: {}", editProductRq);
        model.addAttribute("sections", sections);
        return "/products/edit";
    }
    
    @PostMapping("/update/{menuId}/{id}")
    public String update(
        @PathVariable Long menuId, @PathVariable Long id, EditProductRq editProductRq,
        Principal principal
    ) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user.orElse(null));
        productService.edit(id, editProductRq, menu);
        return "redirect:/products/show/" + menuId + "/" + id;
    }

}
