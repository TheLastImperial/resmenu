package com.thelastimperial.resmenu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thelastimperial.resmenu.controllers.rs.ResmenuRs;
import com.thelastimperial.resmenu.services.ResmenuService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@Controller
@RequestMapping("/resmenu")
public class ResmenuController {
    private final ResmenuService resmenuService;

    @GetMapping("/{id}")
    public String getMenu(@PathVariable Long id, Model model) {
        ResmenuRs menu = resmenuService.get(id);
        model.addAttribute("menu", menu);
        return "/resmenu/show";
    }
}
