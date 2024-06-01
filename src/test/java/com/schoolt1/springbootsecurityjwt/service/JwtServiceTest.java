package com.schoolt1.springbootsecurityjwt.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import com.schoolt1.springbootsecurityjwt.service.impl.JwtServiceImpl;

@ExtendWith(MockitoExtension.class)
    public class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetails userDetails;

    private final String jwtSigningKey = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327856";

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", jwtSigningKey);
    }

    @Test
    public void testGenerateToken() {
        Mockito.when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);
        Assertions.assertNotNull(token);
    }
}