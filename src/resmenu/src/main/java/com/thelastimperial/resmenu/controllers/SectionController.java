package com.thelastimperial.resmenu.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.thelastimperial.resmenu.controllers.rq.EditSectionRq;
import com.thelastimperial.resmenu.controllers.rq.NewSectionRq;
import com.thelastimperial.resmenu.controllers.rs.MenuRs;
import com.thelastimperial.resmenu.controllers.rs.SectionRs;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.SectionEntity;
import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.services.MenuService;
import com.thelastimperial.resmenu.services.SectionService;
import com.thelastimperial.resmenu.services.UserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/sections")
public class SectionController {
    private final UserService userService;
    private final MenuService menuService;
    private final SectionService sectionService;

    @GetMapping("/{menuId}")
    public String index(
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size,
        @PathVariable Long menuId, Principal principal, Model model
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        List<SectionEntity> sections = sectionService.getAll(menu, page, size);
        List<SectionRs> response = sections.stream().map(section ->{
            SectionRs resp = new SectionRs();
            BeanUtils.copyProperties(section, resp);
            return resp;
        }).collect(Collectors.toList());
        MenuRs menuRs = new MenuRs();
        BeanUtils.copyProperties(menu, menuRs);
        model.addAttribute("sections", response);
        model.addAttribute("menuId", menuId);
        model.addAttribute("menu", menuRs);

        return "/sections/index";
    }
    
    @GetMapping("/show/{menuId}/{id}")
    public String show(
        @PathVariable Long menuId, @PathVariable Long id, Principal principal, Model model
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        SectionEntity section = sectionService.get(id, menu);
        SectionRs response = new SectionRs();
        BeanUtils.copyProperties(section, response);
        model.addAttribute("section", response);
        return "/sections/show";
    }
    
    @GetMapping("/new/{menuId}")
    public String newSection(
        @PathVariable Long menuId, Principal principal, NewSectionRq newSectionRq
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        newSectionRq.setMenuId(menu.getId());
        return "/sections/new";
    }

    @PostMapping("/create")
    public String create(
        NewSectionRq newSectionRq, Principal principal
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(newSectionRq.getMenuId(), user);
        sectionService.create(newSectionRq, menu);
        return "redirect:/sections/" + newSectionRq.getMenuId();
    }
    
    @GetMapping("/edit/{menuId}/{id}")
    public String edit(@PathVariable Long menuId, @PathVariable Long id, Principal principal,
        Model model
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        SectionEntity section = sectionService.get(id, menu);
        SectionRs sectionToEdit = new SectionRs();
        BeanUtils.copyProperties(section, sectionToEdit);
        model.addAttribute("section", sectionToEdit);
        model.addAttribute("menuId", menuId);
        return "/sections/edit";
    }

    @PostMapping("/update/{menuId}/{id}")
    public String update(
        @PathVariable Long menuId, @PathVariable Long id, EditSectionRq rq,
        Principal principal
    ) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        sectionService.edit(id, rq, menu);
        return "redirect:/sections/" + menuId;
    }
    
    @GetMapping("/delete/{menuId}/{id}")
    public String delete(@PathVariable Long menuId, @PathVariable Long id, Principal principal) {
        UserEntity user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(menuId, user);
        sectionService.delete(id, menu);
        return "redirect:/sections/" + menuId;
    }
}
