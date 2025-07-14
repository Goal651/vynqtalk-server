package com.vynqtalk.server.service.auth;

import com.vynqtalk.server.dto.response.JwtValidation;
import com.vynqtalk.server.interfaces.IJwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService implements IJwtService {
    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateToken(String email) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + EXPIRATION_TIME;
        return Jwts.builder()
                .claim("sub", email)
                .claim("iat", new Date(nowMillis))
                .claim("exp", new Date(expMillis))
                .signWith(SECRET_KEY)
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) throws AccessDeniedException  {
        JwtValidation result = validateToken(token);
        return result.isValid() ? result.getUsername() : null;
    }

    @Override
    public boolean isTokenValid(String token, String username) throws AccessDeniedException  {
        String extractedUsername = getUsernameFromToken(token);
        return extractedUsername != null && extractedUsername.equals(username);
    }

    @Override
    public JwtValidation validateToken(String token) throws AccessDeniedException {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.get("sub", String.class);
            return new JwtValidation(true, username, null);
        } catch (Exception e) {
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
    }
}