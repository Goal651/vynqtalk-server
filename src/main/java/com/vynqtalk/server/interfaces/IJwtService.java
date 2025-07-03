package com.vynqtalk.server.interfaces;

import com.vynqtalk.server.dto.response.JwtValidation;

public interface IJwtService {
    String generateToken(String email);
    // Existing methods for backwards compatibility
    String getUsernameFromToken(String token);
    boolean isTokenValid(String token, String username);
    
    // New method that returns detailed validation result
    JwtValidation validateToken(String token);
}