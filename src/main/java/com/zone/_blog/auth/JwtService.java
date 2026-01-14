package com.zone._blog.auth;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.zone._blog.users.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    @Value("${spring-security.jwt.secret}")
    private String SECRET;

    @Value("${spring-security.jwt.issuer}")
    private String issuer;

    @Value("${spring-security.jwt.expiration}")
    private long expiration;

    public String generateToken(UserInfo user) {

        return Jwts.builder()
                .setIssuer(this.issuer)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(this.getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getSignatureKey() {
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSignatureKey())
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        try {
            return this.extractClaims(token).getSubject();
        } catch (Exception e) {
            throw new JwtException("Error extarcting username");
        }
    }

    public Date extractExpiration(String token) {
        try {
            return this.extractClaims(token).getExpiration();
        } catch (Exception e) {
            throw new JwtException("Error extarcting token expiration time");
        }
    }

    public boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return this.extractUsername(token).equals(username) && !this.isTokenExpired(token);

    }

}
