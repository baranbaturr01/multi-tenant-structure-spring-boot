package com.baranbatur.appointment_system.multitenant.security;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
@Getter
@Setter
public class JwtUtil {

    @Value("${jwt.secret:secret-key}")
    private String secret;

    @Value("${jwt.expiration:3600000}")
    private long expirationMs;

    public String generateToken(String username) {
        Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }
}