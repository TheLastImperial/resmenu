package com.thelastimperial.resmenu.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username="user", password="1234")
public class SectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    static Long menuId;
    static Long sectionId;
    static String name;

    @BeforeAll
    public void setUp() throws Exception {
        mockMvc.perform(
            post("/menus/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .with(user("user").password("1234"))
            .param("name", "name")
            .param("address", "address")
            .param("phone", "phone")
        )
        .andExpect(
            status().is3xxRedirection()
        )
        .andDo(result -> {
            String url = result.getResponse().getRedirectedUrl();
            String[] urlArray = url.split("/");
            String menuIdStr = urlArray[ urlArray.length - 1];
            assertNotNull(menuIdStr);
            menuId = Long.parseLong(menuIdStr);
            assertNotNull(menuId);
        });
    }

    @Test
    @Order(1)
    public void createSection() throws Exception {
        name = "Section";
        assertNotNull(menuId);
        assertNotNull(name);
        mockMvc.perform(
            post("/sections/create")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("name", name)
            .param("menuId", menuId.toString())
        )
        .andExpect(
            status().is3xxRedirection()
        )
        .andDo(result -> {
            String url = result.getResponse().getRedirectedUrl();
            String[] urlArray = url.split("/");
            String sectionIdStr = urlArray[ urlArray.length - 1 ];
            assertNotNull(sectionIdStr);
            sectionId = Long.parseLong(sectionIdStr);
            assertNotNull(sectionId);
        });
    }

    @Test
    @Order(2)
    public void getSection() throws Exception {
        assertNotNull(menuId);
        assertNotNull(sectionId);
        mockMvc.perform(
            get("/sections/show/" + menuId + "/" + sectionId)
        )
        .andExpect(status().isOk())
        .andExpect(model().attribute("section", 
            allOf(
                hasProperty("id", is(sectionId)),
                hasProperty("name", is(name)),
                hasProperty("menuId", is(menuId))
            )
        ));
    }

    @Test
    @Order(3)
    public void updateSection() throws Exception {
        name = "new name";
        assertNotNull(menuId);
        assertNotNull(sectionId);
        assertNotNull(name);
        mockMvc.perform(
            post("/sections/update/" + menuId + "/" + sectionId)
            .with(csrf())
            .param("name", name)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(
            redirectedUrl("/sections/show/" + menuId + "/" + sectionId));

        mockMvc.perform(
            get("/sections/show/" + menuId + "/" + sectionId)
        )
        .andExpect(status().isOk())
        .andExpect(model().attribute("section", 
            allOf(
                hasProperty("id", is(sectionId)),
                hasProperty("name", is(name)),
                hasProperty("menuId", is(menuId))
            )
        ));
    }
    @Test
    @Order(4)
    public void deleteSection() throws Exception {
        mockMvc.perform(
            get("/sections/delete/" + menuId + "/" + sectionId)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/sections/" + menuId));
    }
}
