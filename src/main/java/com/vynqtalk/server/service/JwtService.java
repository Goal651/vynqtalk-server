package com.vynqtalk.server.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    // Use a fixed secret key (replace with your own and keep it safe)
    private final String SECRET = "your-256-bit-secret-your-256-bit-secret";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public String generateToken(String email) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + EXPIRATION_TIME;
        return Jwts.builder()
                .claim("sub",email)
                .claim("iat", new Date(nowMillis))
                .claim("exp", new Date(expMillis))
                .signWith(SECRET_KEY)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            System.out.println("Token claims: " + claims);
            return claims.get("sub", String.class);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token is expired
            System.out.println("Token signature: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // Token is not a valid JWT
            System.out.println("Malformed token: " + e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            // Token is not supported
            System.out.println("Unsupported token: " + e.getMessage());
        } catch (io.jsonwebtoken.JwtException e) {
            // Generic JWT exception
            System.out.println("JWT error: " + e.getMessage());
        }
        return null;
    }


    public boolean isTokenValid(String token, String username) {
        return getUsernameFromToken(token).equals(username);
    }
}