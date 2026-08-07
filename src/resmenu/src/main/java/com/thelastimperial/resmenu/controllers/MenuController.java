package com.thelastimperial.resmenu.controllers;

import com.thelastimperial.resmenu.repositories.MenuRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thelastimperial.resmenu.controllers.rq.EditMenuRq;
import com.thelastimperial.resmenu.controllers.rq.NewMenuRq;
import com.thelastimperial.resmenu.controllers.rs.MenuRs;
import com.thelastimperial.resmenu.entities.MenuEntity;
import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.services.MenuService;
import com.thelastimperial.resmenu.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(path="/menus")
public class MenuController {
    private final MenuRepository menuRepository;
    private final MenuService menuService;
    private final UserService userService;

    public MenuController(MenuService menuService, UserService userService,
        MenuRepository menuRepository
    ){
        this.menuService = menuService;
        this.userService = userService;
        this.menuRepository = menuRepository;
    }
    
    @GetMapping
    public String index(Model model, Principal principal,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size
    ) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());

        List<MenuRs> menus = menuService
        .getAll(user.orElse(null), page, size).stream().map(ent -> {
            MenuRs rs = new MenuRs();
            BeanUtils.copyProperties(ent, rs);
            return rs;
        }).collect(Collectors.toList());
        model.addAttribute("menus", menus);
        return "menus/index";
    }

    @GetMapping("/new")
    public String newMenu(NewMenuRq menu) {
        return "menus/new";
    }

    @PostMapping("/create")
    public String create(NewMenuRq menu, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            return "menus/new";
        }
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menuCreated = menuService.create(menu, user.orElse(null));
        return "redirect:/menus/show/" + menuCreated.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, Principal principal) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menuEnt = menuService.get(id, user.orElse(null));
        MenuRs rs = new MenuRs();
        BeanUtils.copyProperties(menuEnt, rs);

        model.addAttribute("menu", rs);
        return "menus/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, EditMenuRq editMenuRq, Principal principal) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.update(editMenuRq, id, user.orElse(null));
        return "redirect:/menus/show/" + menu.getId();
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Long id, Principal principal, Model model) {
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menuEntity = menuService.get(id, user.orElse(null));
        MenuRs menuRs = new MenuRs();
        BeanUtils.copyProperties(menuEntity, menuRs);
        model.addAttribute("menu", menuRs);
        return "menus/show";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal){
        Optional<UserEntity> user = userService.getByUsername(principal.getName());
        MenuEntity menu = menuService.get(id, user.orElse(null));
        menuRepository.delete(menu);
        return "redirect:/menus";
    }
}
