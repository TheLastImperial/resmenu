package com.thelastimperial.resmenu.controllers;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thelastimperial.resdomain.entities.MenuEntity;
import com.thelastimperial.resdomain.repositories.MenuRepository;
import com.thelastimperial.resmenu.controllers.rq.EditMenuRq;
import com.thelastimperial.resmenu.controllers.rq.NewMenuRq;
import com.thelastimperial.resmenu.controllers.rs.MenuRs;
import com.thelastimperial.resmenu.controllers.rs.ProductRs;
import com.thelastimperial.resmenu.controllers.rs.SectionRs;
import com.thelastimperial.resmenu.services.MenuService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(path="/menus")
@Slf4j
public class MenuController {
    private final MenuRepository menuRepository;
    private final MenuService menuService;

    public MenuController(MenuService menuService,
        MenuRepository menuRepository
    ){
        this.menuService = menuService;
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public String index(Model model, Principal principal,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Principal: {}", principal.getName());

        UUID userId = UUID.fromString(principal.getName());
        Page<MenuEntity> pageData = menuService.getAll(userId, page, size);
        List<MenuRs> menus = pageData.stream().map(ent -> {
            MenuRs rs = new MenuRs();
            BeanUtils.copyProperties(ent, rs);
            return rs;
        }).collect(Collectors.toList());

        model.addAttribute("menus", menus);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalElements", pageData.getTotalElements());
        model.addAttribute("currentPage", pageData.getNumber());
        model.addAttribute("totalPages", pageData.getTotalPages());
        return "menus/index";
    }

    @GetMapping("/new")
    public String newMenu(NewMenuRq menu) {
        return "menus/new";
    }

    @PostMapping("/create")
    public String create(
        @Valid NewMenuRq menu, BindingResult result, Model model, Principal principal
    ) {
        if (result.hasErrors()) {
            return "menus/new";
        }
        UUID userId = UUID.fromString(principal.getName());
        MenuEntity menuCreated = menuService.create(menu, userId);
        return "redirect:/menus/show/" + menuCreated.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(
        @PathVariable Long id, EditMenuRq editMenuRq, Model model,
        Principal principal
    ) {
        UUID userId = UUID.fromString(principal.getName());
        MenuEntity menuEnt = menuService.get(id, userId);
        BeanUtils.copyProperties(menuEnt, editMenuRq);

        return "menus/edit";
    }

    @PostMapping("/update/{id}")
    public String update(
        @PathVariable Long id, @Valid EditMenuRq editMenuRq, BindingResult result,
        Principal principal
    ) {
        if(result.hasErrors()){
            return "menus/edit";
        }
        UUID userId = UUID.fromString(principal.getName());
        MenuEntity menu = menuService.update(editMenuRq, id, userId);
        return "redirect:/menus/show/" + menu.getId();
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Long id, Principal principal, Model model) {
        UUID userId = UUID.fromString(principal.getName());
        MenuEntity menuEntity = menuService.get(id, userId);
        MenuRs menuRs = new MenuRs();
        BeanUtils.copyProperties(menuEntity, menuRs);
        menuEntity.getSections().stream().forEach(sect -> {
            SectionRs section = new SectionRs();
            BeanUtils.copyProperties(sect, section);
            sect.getProducts().stream().forEach(prod -> {
                ProductRs product = new ProductRs();
                BeanUtils.copyProperties(prod, product);
                section.getProducts().add(product);
            });
            menuRs.getSections().add(section);
        });
        model.addAttribute("menu", menuRs);
        return "menus/show";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal){
        UUID userId = UUID.fromString(principal.getName());
        MenuEntity menu = menuService.get(id, userId);
        menuRepository.delete(menu);
        return "redirect:/menus";
    }
}
