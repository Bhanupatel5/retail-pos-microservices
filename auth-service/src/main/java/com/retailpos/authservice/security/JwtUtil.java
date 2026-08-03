package com.retailpos.authservice.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.retailpos.authservice.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Generate JWT Token
     */
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", user.getRole().name());
        claims.put("name", user.getName());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmployeeId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extract Employee ID (Subject)
     */
    public String extractEmployeeId(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Validate JWT Token
     */
    public boolean validateToken(String token, UserDetails userDetails) {

        String employeeId = extractEmployeeId(token);

        return employeeId.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
    /**
     * Check whether token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    /**
     * Extract all claims from JWT
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generate Signing Key
     */
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
    }
}