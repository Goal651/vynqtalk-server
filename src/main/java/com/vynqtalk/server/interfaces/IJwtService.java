package com.vynqtalk.server.interfaces;


import org.springframework.security.access.AccessDeniedException;

import com.vynqtalk.server.dto.response.JwtValidation;

public interface IJwtService {
    String generateToken(String email);

    String getUsernameFromToken(String token) throws AccessDeniedException;

    boolean isTokenValid(String token, String username) throws AccessDeniedException;

    JwtValidation validateToken(String token) throws AccessDeniedException;
}