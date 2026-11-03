package com.climate_action.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String secretKey = System.getenv("JWT_SECRET");

    private SecretKey getSigningKey() {

        if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must be set and contain at least 32 characters"
            );
        }

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
    }

    public String generateToken(String email) {

        long expirationTime = 1000 * 60 * 60 * 24; // 24 hours

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, String email) {

        try {
            String extractedEmail = extractEmail(token);
            return extractedEmail.equals(email);
        } catch (Exception exception) {
            return false;
        }
    }
}
