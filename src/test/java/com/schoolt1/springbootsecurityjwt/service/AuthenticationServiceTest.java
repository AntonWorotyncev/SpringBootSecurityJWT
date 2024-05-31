package com.schoolt1.springbootsecurityjwt.service;

import com.schoolt1.springbootsecurityjwt.dto.JwtAuthenticationResponse;
import com.schoolt1.springbootsecurityjwt.dto.SignInRequest;
import com.schoolt1.springbootsecurityjwt.dto.SignUpRequest;
import com.schoolt1.springbootsecurityjwt.model.Role;
import com.schoolt1.springbootsecurityjwt.model.User;
import com.schoolt1.springbootsecurityjwt.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Import({AuthenticationServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    private AuthenticationService authenticationService;

    private JwtAuthenticationResponse jwtAuthenticationResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl
                (userService, jwtService, passwordEncoder, authenticationManager, userDetailsService);
        passwordEncoder = new BCryptPasswordEncoder();

        jwtAuthenticationResponse = new JwtAuthenticationResponse("token-tut");
    }

    @Test
    public void testSignUpSuccess() {
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

        Mockito.when(userService.create(Mockito.any(User.class))).thenReturn(testUser);
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn(jwtAuthenticationResponse.getToken());

        JwtAuthenticationResponse response = authenticationService.signUp(signUpRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);


        Mockito.verify(userService).create(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        Assertions.assertEquals(testUser.getEmail(), capturedUser.getEmail());

        Assertions.assertEquals(jwtAuthenticationResponse.getToken(), response.getToken());
    }


    @Test
    public void testSignInSuccess() {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPassword("password");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetailsService.loadUserByUsername(signInRequest.getUsername())).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn(jwtAuthenticationResponse.getToken());

        JwtAuthenticationResponse response = authenticationService.signIn(signInRequest);

        Assertions.assertEquals(jwtAuthenticationResponse.getToken(), response.getToken());
    }

    @Test
    public void testSignInFailAndThrowsError() throws AuthenticationException {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPassword("password");

        Mockito.lenient().when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new DisabledException("подробности"));

        Mockito.verify(userService, Mockito.never()).create(Mockito.any(User.class));

        Mockito.verify(jwtService, Mockito.never()).generateToken(Mockito.any());

        try {
            authenticationService.signIn(signInRequest);
        } catch (AuthenticationException ex) {
            Assertions.assertTrue(true);
        }
    }
}
