package com.tshirtshop.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // ────────────────────────────────────────────────
    // 1. Clé lue dans application.properties
    // ────────────────────────────────────────────────
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ────────────────────────────────────────────────
    // 2. Durée de vie du token : 10 h
    // ────────────────────────────────────────────────
    private static final long EXPIRATION = 10 * 60 * 60 * 1000;

    // ────────────────────────────────────────────────
    // 3. Génération
    // ────────────────────────────────────────────────
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ────────────────────────────────────────────────
    // 4. Extraction d’info
    // ────────────────────────────────────────────────
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ────────────────────────────────────────────────
    // 5. Validation
    // ────────────────────────────────────────────────
    public boolean isTokenValid(String token, String userEmail) {
        return userEmail.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // ────────────────────────────────────────────────
    // 6. Méthodes internes
    // ────────────────────────────────────────────────
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
