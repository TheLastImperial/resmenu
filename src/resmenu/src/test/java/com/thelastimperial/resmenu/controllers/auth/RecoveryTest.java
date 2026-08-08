package com.thelastimperial.resmenu.controllers.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class RecoveryTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void recoveryAccount() throws Exception {
        mockMvc.perform(
            post("/auth/recovery")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("username", "newpassword@email.com")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/recovery?generated=true"));
    }
    @Test
    @Order(2)
    public void updatePasswordWrongTokenPattern() throws Exception {
        mockMvc.perform(
            post("/auth/new_password")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("password", "1234asdfAS@")
            .param("passwordConfirmation", "1234asdfAS@")
            .param("token", "asdfgfdg")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/invalid-token"));
    }
    @Test
    @Order(2)
    public void updatePasswordTokenDontExists() throws Exception {
        mockMvc.perform(
            post("/auth/new_password")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("password", "1234asdfAS@")
            .param("passwordConfirmation", "1234asdfAS@")
            .param("token", "2930b83d-354f-4d6e-b5e2-9ff7e01fce00")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/invalid-token"));
    }
    @Test
    @Order(2)
    public void updatePasswordExpiredToken() throws Exception {
        mockMvc.perform(
            post("/auth/new_password")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("password", "1234asdfAS@")
            .param("passwordConfirmation", "1234asdfAS@")
            .param("token", "2930b83d-354f-4d6e-b5e2-9ff7e01fce24")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/invalid-token"));
    }
    @Test
    @Order(3)
    public void updatePassword() throws Exception {
        mockMvc.perform(
            post("/auth/new_password")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("password", "1234asdfAS@")
            .param("passwordConfirmation", "1234asdfAS@")
            .param("token", "2930b83d-354f-4d6e-b5e2-9ff7e01fce25")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/login"));
    }
    @Test
    @Order(4)
    public void updatePasswordTokenUsed() throws Exception {
        mockMvc.perform(
            post("/auth/new_password")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("password", "1234asdfAS@")
            .param("passwordConfirmation", "1234asdfAS@")
            .param("token", "2930b83d-354f-4d6e-b5e2-9ff7e01fce25")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/invalid-token"));
    }

    @Test
    @Order(5)
    public void loginTryLastPassword() throws Exception {
        mockMvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("username", "newpassword")
            .param("password","1234")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/login?error=true"));
    }

    @Test
    @Order(6)
    public void loginWithNewPassword() throws Exception {
        mockMvc.perform(
            post("/auth/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .with(csrf())
            .param("username", "newpassword")
            .param("password","1234asdfAS@")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
    }
}
