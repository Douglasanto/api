package com.apiIc.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;
    
    private static final int MIN_KEY_LENGTH = 64; // 512 bits for HS512
    private byte[] secretKeyBytes;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        if (secretKeyBytes == null) {
            // Ensure the secret key is properly encoded and has sufficient length
            String key = secret;
            while (key.getBytes().length < MIN_KEY_LENGTH) {
                key = key + key; // Double the key until it's long enough
            }
            secretKeyBytes = key.getBytes();
        }
        return Jwts.parserBuilder()
                .setSigningKey(secretKeyBytes)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

private String doGenerateToken(Map<String, Object> claims, String subject) {
        if (secretKeyBytes == null) {
            // Ensure the secret key is properly encoded and has sufficient length
            String key = secret;
            while (key.getBytes().length < MIN_KEY_LENGTH) {
                key = key + key; // Double the key until it's long enough
            }
            secretKeyBytes = key.getBytes();
        }
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKeyBytes), SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            getUsernameFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
