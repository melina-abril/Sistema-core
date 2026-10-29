package com.empresa.sistemacore.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final long SESSION_MINUTES = 30;
    private final SecretKey signingKey;
    private final ConcurrentMap<String, Instant> revokedIds = new ConcurrentHashMap<>();

    public JwtService(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("JWT_SECRET debe contener al menos 32 bytes");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public record TokenData(String username, String name, Roles role, String jti, Instant expiresAt) {
    }

    public record IssuedToken(String token, Instant expiresAt) {
    }

    public IssuedToken issue(String username, String name, Roles role) {
        if (username == null || username.isBlank() || name == null || name.isBlank() || role == null) {
            throw new IllegalArgumentException("El usuario, nombre y rol son obligatorios");
        }
        Instant now = Instant.now();
        Instant expiresAt = now.plus(SESSION_MINUTES, ChronoUnit.MINUTES);
        String token = Jwts.builder()
                .subject(username)
                .claim("name", name)
                .claim("role", role.name())
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
        return new IssuedToken(token, expiresAt);
    }

    public TokenData validate(String token) {
        purgeExpiredRevocations();
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.getSubject();
            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);
            String jti = claims.getId();
            Date expiration = claims.getExpiration();
            if (username == null || username.isBlank() || name == null || name.isBlank()
                    || role == null || jti == null || jti.isBlank() || expiration == null
                    || !expiration.toInstant().isAfter(Instant.now()) || revokedIds.containsKey(jti)) {
                throw new BadCredentialsException("Token inválido o revocado");
            }
            return new TokenData(username, name, Roles.valueOf(role), jti, expiration.toInstant());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new BadCredentialsException("Token inválido o vencido", exception);
        }
    }

    public IssuedToken renew(String token) {
        TokenData current = validate(token);
        return issue(current.username(), current.name(), current.role());
    }

    public void revoke(String token) {
        TokenData current = validate(token);
        revokedIds.put(current.jti(), current.expiresAt());
    }

    private void purgeExpiredRevocations() {
        Instant now = Instant.now();
        revokedIds.entrySet().removeIf(entry -> !entry.getValue().isAfter(now));
    }
}
