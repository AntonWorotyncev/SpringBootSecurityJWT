package com.schoolt1.springbootsecurityjwt.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolt1.springbootsecurityjwt.config.JwtAuthenticationFilter;
import com.schoolt1.springbootsecurityjwt.config.SecurityConfig;
import com.schoolt1.springbootsecurityjwt.dto.JwtAuthenticationResponse;
import com.schoolt1.springbootsecurityjwt.dto.SignInRequest;
import com.schoolt1.springbootsecurityjwt.dto.SignUpRequest;
import com.schoolt1.springbootsecurityjwt.model.Role;
import com.schoolt1.springbootsecurityjwt.model.User;
import com.schoolt1.springbootsecurityjwt.service.JwtService;
import com.schoolt1.springbootsecurityjwt.service.UserService;
import com.schoolt1.springbootsecurityjwt.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import({SecurityConfig.class, AuthenticationServiceImpl.class, JwtAuthenticationFilter.class})
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private JwtAuthenticationResponse jwtResponse;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("test@mail.com");

        signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("password");

        jwtResponse = new JwtAuthenticationResponse();
        jwtResponse.setToken("jwt-token");
    }

    @Test
    void signUp() throws Exception {
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn(jwtResponse.getToken());
        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.jsonPath("$.token")
                .value(jwtResponse.getToken()));
    }

    @Test
    void signIn() throws Exception {
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(User.builder()
                .username(signInRequest.getUsername()).email("kaktotak@gmaiil.com")
                .role(Role.ROLE_USER).password("1111").id(1L).build());

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(Mockito.mock(Authentication.class));


        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn(jwtResponse.getToken());

        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(jwtResponse.getToken()));
    }
}