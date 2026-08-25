package com.thelastimperial.resmenu.controllers;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser(username = "user", password = "1234")
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    static Long menuId;
    static Long sectionId;

    static Long productId;
    static String name;
    static String description;
    static String price;

    @BeforeAll
    public void setUp() throws Exception {
        mockMvc.perform(
            post("/menus/create")
            .with(csrf())
            .with(user("user").password("1234"))
            .param("name", "Name")
            .param("address", "Address")
            .param("phone","1234")
        )
        .andExpect(status().is3xxRedirection())
        .andDo(result -> {
            String url = result.getResponse().getRedirectedUrl();
            String[] urlArr = url.split("/");
            String menuIdStr = urlArr[urlArr.length - 1];
            assertNotNull(menuIdStr);
            menuId = Long.parseLong(menuIdStr);
            assertNotNull(menuId);
        });

        mockMvc.perform(
            post("/sections/create")
            .with(csrf())
            .with(user("user").password("1234"))
            .param("name", "Section")
            .param("menuId", menuId.toString())
        )
        .andExpect(status().is3xxRedirection())
        .andDo(result -> {
            String url = result.getResponse().getRedirectedUrl();
            String[] urlArr = url.split("/");
            String sectionIdStr = urlArr[urlArr.length - 1];
            assertNotNull(sectionIdStr);
            sectionId = Long.parseLong(sectionIdStr);
            assertNotNull(sectionId);
        });
    }

    @Test
    @Order(1)
    public void createProduct() throws Exception {
        name = "Product";
        description = "Description";
        price = "120";
        mockMvc.perform(
            post("/products/create")
            .with(csrf())
            .param("name", name)
            .param("description", description)
            .param("price", price)
            .param("sectionId", sectionId.toString())
            .param("menuId", menuId.toString())
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/products/" + menuId + "/*" ))
        .andDo(result -> {
            String url = result.getResponse().getRedirectedUrl();
            String[] urlArr = url.split("/");
            String productIdStr = urlArr[urlArr.length - 1];
            assertNotNull(productIdStr);
            productId = Long.parseLong(productIdStr);
            assertNotNull(productId);
        });
    }
    @Test
    @Order(2)
    public void getProduct() throws Exception {
        mockMvc.perform(
            get(
                String.format("/products/%d/%d", menuId, productId)
            )
        )
        .andExpect(status().isOk())
        .andExpect(model().attribute("menuId", menuId))
        .andExpect(model().attribute("product",
            allOf(
                hasProperty("id", is(productId)),
                hasProperty("name", is(name)),
                hasProperty("description", is(description)),
                hasProperty("price", is(Double.parseDouble(price))))
            )
        );
    }

    @Test
    @Order(3)
    public void updateProduct() throws Exception {
        name = "Product2";
        description = "New Description";
        price = "140";
        mockMvc.perform(
            post(String.format("/products/update/%d/%d", menuId, productId))
            .with(csrf())
            .param("name",name)
            .param("description", description)
            .param("price", price)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(String.format("/products/%d/%d", menuId, productId)));

        mockMvc.perform(
            get(String.format("/products/%d/%d", menuId, productId))
        )
        .andExpect(status().isOk())
        .andExpect(model().attribute("product", 
            allOf(
                hasProperty("id", is(productId)),
                hasProperty("name", is(name)),
                hasProperty("description", is(description)),
                hasProperty("price", is(Double.parseDouble(price)))
            )
        ));
    }

    @Test
    @Order(4)
    public void deleteProduct() throws Exception {
        mockMvc.perform(
            get(String.format("/products/%d/%d", menuId, productId))
        )
        .andExpect(status().isOk());
    }
}
