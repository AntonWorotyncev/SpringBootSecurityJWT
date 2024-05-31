package com.schoolt1.springbootsecurityjwt.controller;

import com.schoolt1.springbootsecurityjwt.config.JwtAuthenticationFilter;
import com.schoolt1.springbootsecurityjwt.config.SecurityConfig;
import com.schoolt1.springbootsecurityjwt.service.AuthenticationService;
import com.schoolt1.springbootsecurityjwt.service.JwtService;
import com.schoolt1.springbootsecurityjwt.service.UserService;
import com.schoolt1.springbootsecurityjwt.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import({SecurityConfig.class, AuthenticationServiceImpl.class, JwtAuthenticationFilter.class})
@WebMvcTest(ContentController.class)
public class ContentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationService authenticationService;

    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    public void getSecretContent_AuthorizedUser_ShouldReturnSecretContent() throws Exception {
        mockMvc.perform(get("/resource"))
                .andExpect(status().isOk())
                .andExpect(content().string("Secret content"));
    }

    @Test
    public void getSecretContent_UnauthorizedUser_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/resource"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAdminSecretContent_AdminUser_ShouldReturnAdminSecretContent() throws Exception {
        mockMvc.perform(get("/resource/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin secret content"));
    }

    @Test
    public void getAdminSecretContent_UnauthorizedUser_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/resource/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAdminSecretContent_NonAdminUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/resource/admin"))
                .andExpect(status().isForbidden());
    }
}