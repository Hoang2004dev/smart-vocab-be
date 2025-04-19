package com.smartvocab.smart_vocab_backend.security;

import com.smartvocab.smart_vocab_backend.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    //@Value("${jwt.secret}")
    private String jwtSecret = "TrZJ4Wv6uUdmnKpsWbhkmCRxjYEXjO4vKsW6EdbYw9up9mxH8gM3Go3X5pdD2B196xertQxNzi9ZzFQF5nbSBg==";

    //@Value("${jwt.expiration.access}")
    private long jwtAccessExpirationMs = 9000000; // 15 minutes

    //@Value("${jwt.expiration.refresh}")
    //private long jwtRefreshExpirationMs = 604800000; // 7 days

    private SecretKey getSigningKey() {
        // Giải mã Base64 thành mảng byte
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);

        // Kiểm tra chiều dài của key (tối thiểu là 64 bytes = 512 bits)
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("Key size should be at least 512 bits (64 bytes).");
        }

        // Trả về khóa ký đã được tạo
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String userId, String email) {
        return generateToken(userId, email, jwtAccessExpirationMs);
    }

//    public String generateRefreshToken(String userId, String email) {
//        return generateToken(userId, email, jwtRefreshExpirationMs);
//    }

    private String generateToken(String userId, String email, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new JwtAuthenticationException("Invalid JWT token: " + e.getMessage());
        }
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
