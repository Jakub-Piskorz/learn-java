package com.fastfile.auth;

import com.fastfile.config.GlobalVariables;
import com.fastfile.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Autowired
    private GlobalVariables env;

    protected SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(env.secretKey().getBytes());
    }


    public String generateToken(User user) {
        return generateToken(user, new HashMap<>());
    }
    public String generateToken(User user, Map<String, Object> extraClaims) {
        long expirationMs = 3600000;
        extraClaims.put("userId", user.getId()); // Used for user folders in file system, therefore always a claim.
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return userDetails.getUsername().equals(username) && isTokenExpired(token);
    }

    public String extractUsername(String token) {
        final Claims claims = extractClaims(token);
        return claims.getSubject();
    }

//    public Object extractUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            return authentication.getCredentials(); // Returns the username
//        }
//        return null; // No user is authenticated
//    }

}