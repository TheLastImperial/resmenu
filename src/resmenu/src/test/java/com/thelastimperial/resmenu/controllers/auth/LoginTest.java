package com.thelastimperial.resmenu.controllers.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginWithUser() throws Exception {
        mockMvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("username", "user")
            .param("password","1234")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
    }
    @Test
    public void loginWithEmail() throws Exception {
        mockMvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("username", "user@email.com")
            .param("password","1234")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
    }

    @Test
    public void loginWrongPassword() throws Exception {
        mockMvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("username", "user")
            .param("password","asdf")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/login?error=true"));
    }
    @Test
    public void loginWithRememeberMe() throws Exception {
        mockMvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("username", "user")
            .param("password","asdf")
            .param("rememeber-me","true")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(cookie().exists("remember-me"));
    }
}
