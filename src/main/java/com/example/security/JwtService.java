package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;


    @Value("${jwt.expiration-ms:3600000}")
    private long expirationTime;


    private SecretKey signingKey;


    @PostConstruct
    public void init() {

        if (secretKey == null
                || secretKey.getBytes(
                        StandardCharsets.UTF_8
                ).length < 32) {

            throw new IllegalStateException(
                    "JWT secret must contain at least 32 bytes"
            );
        }


        signingKey =
                Keys.hmacShaKeyFor(
                        secretKey.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
    }


    public String generateToken(
            String username,
            String role) {

        Map<String, Object> claims =
                new HashMap<>();

        claims.put(
                "role",
                role
        );


        return Jwts.builder()

                .claims(claims)

                .subject(username)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expirationTime
                        )
                )

                .signWith(signingKey)

                .compact();
    }


    public String extractUsername(
            String token) {

        return getClaims(token)
                .getSubject();
    }


    public String extractRole(
            String token) {

        return getClaims(token)
                .get("role", String.class);
    }


    public boolean isTokenValid(
            String token,
            String username) {

        String tokenUsername =
                extractUsername(token);

        return tokenUsername.equals(username)
                && !isTokenExpired(token);
    }


    private boolean isTokenExpired(
            String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }


    private Claims getClaims(
            String token) {

        return Jwts.parser()

                .verifyWith(signingKey)

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }
}