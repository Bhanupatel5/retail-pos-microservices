package com.retailpos.apigateway.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Extract Employee ID from JWT subject
     */
    public String extractEmployeeId(String token) {

        return extractAllClaims(token).getSubject();
    }

    /**
     * Extract Role from JWT
     */
    public String extractRole(String token) {

        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Check whether JWT is expired
     */
    public boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    /**
     * Validate JWT
     */
    public boolean validateToken(String token) {

        try {

            extractAllClaims(token);

            return !isTokenExpired(token);

        } catch (Exception ex) {

            return false;
        }
    }

    /**
     * Extract all claims and verify JWT signature
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generate signing key
     */
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
    }
}