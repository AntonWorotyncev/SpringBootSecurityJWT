package com.schoolt1.springbootsecurityjwt.service;

import com.schoolt1.springbootsecurityjwt.dto.JwtAuthenticationResponse;
import com.schoolt1.springbootsecurityjwt.dto.SignInRequest;
import com.schoolt1.springbootsecurityjwt.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);
    JwtAuthenticationResponse signIn(SignInRequest request);
}
