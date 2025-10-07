package com.example.swp.Config;

import com.example.swp.Entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
@Component
public class JwtUtil {
    private final SecretKey key;

    public JwtUtil() {
        String secret = "MyVerySecretKeyThatIsLongEnough123456789";
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserEntity userEntity) {
        return Jwts.builder()
                .setSubject(userEntity.getUserName())
                .claim("email", userEntity.getEmail())
                .claim("user_name", userEntity.getUserName())
                .claim("role", userEntity.getRole().name())
                .claim("id", userEntity.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    

    public String extractUsername(String token) {
        return parseAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Claims claims = parseAllClaims(token);
        return claims.get("id", Long.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
                && !parseAllClaims(token).getExpiration().before(new Date()));
    }

}
