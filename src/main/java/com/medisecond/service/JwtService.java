package com.medisecond.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(UserDetails u) {
        return Jwts.builder().setSubject(u.getUsername())
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String t, UserDetails u) {
        return extractUsername(t).equals(u.getUsername()) && !isExpired(t);
    }

    public String extractUsername(String t) {
        return claim(t, Claims::getSubject);
    }

    private boolean isExpired(String t) {
        return claim(t, Claims::getExpiration).before(new Date());
    }

    private <T> T claim(String t, Function<Claims, T> r) {
        return r.apply(Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(t).getBody());
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
