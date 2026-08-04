package com.thelastimperial.resmenu.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thelastimperial.resmenu.controllers.rq.EditProductRq;
import com.thelastimperial.resmenu.controllers.rq.NewProductRq;
import com.thelastimperial.resmenu.controllers.rs.MenuRs;
import com.thelastimperial.resmenu.controllers.rs.ProductRs;
import com.thelastimperial.resmenu.controllers.rs.SectionRs;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.ProductEntity;
import com.thelastimperial.resmenu.entities.SectionEntity;
import com.thelastimperial.resmenu.entities.StorageEntity;
import com.thelastimperial.resmenu.entities.UserEntity;
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

    @GetMapping("/{menuId}/{id}")
    public String get(@PathVariable Long menuId, @PathVariable Long id, Principal principal,
        Model model
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
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
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        List<ProductRs> products = productService.getAll(menu, page, size).stream().map(prod -> {
            ProductRs resp = new ProductRs();
            BeanUtils.copyProperties(prod, resp);
            return resp;
        }).collect(Collectors.toList());

        MenuRs menuRs = new MenuRs();
        BeanUtils.copyProperties(menu, menuRs);
        model.addAttribute("products", products);
        model.addAttribute("menuId", menuId);
        model.addAttribute("menu", menuRs);
        return "/products/index";
    }
    
    @GetMapping("/new/{menuId}")
    public String newProduct(@PathVariable Long menuId, NewProductRq newProductRq, Principal principal, Model model) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);        
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
    public String create(NewProductRq rq, BindingResult result, Principal principal) throws Exception{
        if(result.hasErrors()){
            log.error("Errors: {}", result);
            return "redirect:/products/new/" + rq.getMenuId();
        }

        if(rq.getImage() != null){
            StorageEntity storageEntity = storageService.create(rq.getImage());
            rq.setImageId(storageEntity.getId());
        }

        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(rq.getMenuId(), user);
        SectionEntity section = sectionService.get(rq.getSectionId(), menu);
        ProductEntity product = productService.create(rq, menu, section);
        return "redirect:/products/" + menu.getId() + "/" + product.getId();
    }

    @GetMapping("/delete/{menuId}/{id}")
    public String delete(@PathVariable Long menuId, @PathVariable Long id, Principal principal) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(id, user);
        productService.delete(id, menu);
        return "redirect:/products/menu/" + menuId;
    }

    @GetMapping("/edit/{menuId}/{id}")
    public String edit(@PathVariable Long menuId, @PathVariable Long id,
        Model model, Principal principal
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(id, user);
        ProductEntity product = productService.get(id, menu);
        ProductRs response = new ProductRs();
        BeanUtils.copyProperties(product, response);
        model.addAttribute("product", response);
        return "/products/edit";
    }
    
    @PostMapping("/update/{menuId}/{id}")
    public String update(@PathVariable Long menuId, @PathVariable Long id, Principal principal,
        EditProductRq rq
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        productService.edit(id, rq, menu);
        return "redirect:/products/" + menuId + "/" + id;
    }
    
}
