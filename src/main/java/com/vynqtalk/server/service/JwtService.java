package com.vynqtalk.server.service;

import com.vynqtalk.server.model.response.JwtValidationResult;
import com.vynqtalk.server.interfaces.IJwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService implements IJwtService {
    private final String SECRET = "your-256-bit-secret-your-256-bit-secret";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

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
    public String getUsernameFromToken(String token) {
        JwtValidationResult result = validateToken(token);
        return result.isValid() ? result.getUsername() : null;
    }

    @Override
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = getUsernameFromToken(token);
        return extractedUsername != null && extractedUsername.equals(username);
    }

    @Override
    public JwtValidationResult validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.get("sub", String.class);
            return new JwtValidationResult(true, username, null);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            return new JwtValidationResult(false, null,
                    "JWT signature does not match locally computed signature. " + e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return new JwtValidationResult(false, null, "Token expired");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            return new JwtValidationResult(false, null, "Malformed token: " + e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            return new JwtValidationResult(false, null, "Unsupported token: " + e.getMessage());
        } catch (io.jsonwebtoken.JwtException e) {
            return new JwtValidationResult(false, null, "JWT error: " + e.getMessage());
        }
        catch (Exception e) {
            return new JwtValidationResult(false, null, "Unexpected error: " + e.getMessage());
        }
    }
}