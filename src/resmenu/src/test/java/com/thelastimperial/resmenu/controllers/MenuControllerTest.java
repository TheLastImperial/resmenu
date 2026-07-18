package com.thelastimperial.resmenu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@WithMockUser(username = "user", password = "1234")
public class MenuControllerTest {
    @Autowired
    public MockMvc mockMvc;
    static String menuId;
    static String name;
    static String address;
    static String phone;
    
    @Test
    @Order(1)
    public void createMenu() throws Exception{
        name = "Sushie";
        address = "Direccion";
        phone = "1234";
        mockMvc.perform(
            post("/menus/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("name", name)
            .param("address", address)
            .param("phone", phone)
        )
        .andExpect(status().is3xxRedirection())

        .andDo(result -> {
            String url = result.getResponse().getRedirectedUrl();
            String[] urlArray = url.split("/");
            menuId = urlArray[ urlArray.length - 1];
            assertNotNull(menuId);
        });
    }
    @Test
    @Order(2)
    public void showMenu() throws Exception {
        assertNotNull(menuId);
        mockMvc.perform(
            get("/menus/show/" + menuId)
        )
        .andExpect(status().isOk())
        .andExpect(model().attribute("menu", 
            allOf(
                hasProperty("name", is(name)),
                hasProperty("address", is(address)),
                hasProperty("phone", is(phone))
            )
        ));
    }

    @Test
    @Order(3)
    public void updateMenu() throws Exception {
        assertNotNull(menuId);
        name = "Supersito";
        address = "Dis";
        phone = "1234";

        mockMvc.perform(
            post("/menus/update/" + menuId)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("name", name)
            .param("address", address)
            .param("phone", phone)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/show/*"));

        mockMvc.perform(
            get("/menus/show/" + menuId)
        )
        .andExpect(status().isOk())
        .andExpect(model().attribute("menu", 
            allOf(
                hasProperty("name", is(name)),
                hasProperty("address", is(address)),
                hasProperty("phone", is(phone))
            )
        ));
    }

    @Test
    @Order(4)
    public void deleteMenu() throws Exception {
        assertNotNull(menuId);
        mockMvc.perform(
            get("/menus/delete/" + menuId)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/menus"));
    }
}
