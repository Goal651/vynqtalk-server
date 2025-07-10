package com.vynqtalk.server.interfaces;

import com.vynqtalk.server.dto.response.JwtValidation;

public interface IJwtService {
    String generateToken(String email);
    String getUsernameFromToken(String token);
    boolean isTokenValid(String token, String username);
    JwtValidation validateToken(String token);
}