package com.schoolt1.springbootsecurityjwt.service;

import com.schoolt1.springbootsecurityjwt.dto.JwtAuthenticationResponse;
import com.schoolt1.springbootsecurityjwt.dto.SignInRequest;
import com.schoolt1.springbootsecurityjwt.dto.SignUpRequest;
import com.schoolt1.springbootsecurityjwt.model.Role;
import com.schoolt1.springbootsecurityjwt.model.User;
import com.schoolt1.springbootsecurityjwt.service.impl.AuthenticationServiceImpl;
import com.schoolt1.springbootsecurityjwt.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@Import({AuthenticationServiceImpl.class, JwtServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;


    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtServiceImpl();
        authenticationService = new AuthenticationServiceImpl
                (userService,jwtService,passwordEncoder,authenticationManager,userDetailsService);
    }

    @Test
    public void testSignUp() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testUser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");

        User testUser = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password("encodedPassword")
                .role(Role.ROLE_ADMIN)
                .build();

        Mockito.when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userService.create(Mockito.any(User.class))).thenReturn(testUser);
        //Mockito.when(jwtService.getSigningKey()).thenReturn("abrakadabra");

        JwtAuthenticationResponse response = authenticationService.signUp(signUpRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());

        Mockito.verify(passwordEncoder).encode(signUpRequest.getPassword());
        Mockito.verify(userService).create(Mockito.any(User.class));
        Mockito.verify(jwtService).generateToken(Mockito.any(User.class));
    }


    @Test
    public void testSignIn() {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPassword("password");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetailsService.loadUserByUsername(signInRequest.getUsername())).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("jwtToken");

        Mockito.doNothing().when(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        JwtAuthenticationResponse response = authenticationService.signIn(signInRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());

        verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(signInRequest.getUsername());
        verify(jwtService).generateToken(userDetails);
    }
}
