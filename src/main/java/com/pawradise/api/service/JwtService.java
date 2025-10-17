package com.pawradise.api.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generateToken(String email);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
