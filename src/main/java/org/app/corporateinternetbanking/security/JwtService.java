package org.app.corporateinternetbanking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.app.corporateinternetbanking.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service

public class JwtService {


    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private String extractClaim(String token, String keyName) {

        return extractAllClaims(token).get(keyName).toString();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return isTokenExpired(extractAllClaims(token));

    }

    private SecretKey getSecretKey() {
        byte[] encodedKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(encodedKey);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getEmail());
        claims.put("role", user.getRole());
        return Jwts.builder().claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSecretKey())
                .compact();


    }

    public String generateEmailToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "EMAIL_VERIFICATION");

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (3600000)))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean isEmailTokenValid(String token) {
        var claims = extractAllClaims(token);

        if (!"EMAIL_VERIFICATION".equals(claims.get("type"))) {
            return false;
        }
        return !isTokenExpired(claims);
    }

    public String extractEmailFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }
}