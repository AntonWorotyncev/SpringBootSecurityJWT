package com.schoolt1.springbootsecurityjwt.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolvers);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    Claims extractAllClaims(String token);

    Key getSigningKey();
}


