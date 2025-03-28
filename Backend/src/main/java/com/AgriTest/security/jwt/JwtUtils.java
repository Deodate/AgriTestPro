// src/main/java/com/AgriTest/security/jwt/JwtUtils.java
package com.AgriTest.security.jwt;

import com.AgriTest.security.service.UserDetailsImpl;
import com.AgriTest.service.TokenBlacklistService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationMs;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        // Trim the token to remove any whitespace
        token = token.trim();
        
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            // Check if token is null or empty
            if (authToken == null || authToken.isEmpty()) {
                logger.error("JWT token is null or empty");
                return false;
            }
            
            // Trim the token to remove any whitespace
            authToken = authToken.trim();
            
            // Check for spaces within token
            if (authToken.contains(" ")) {
                logger.error("JWT token contains spaces which are not allowed");
                return false;
            }
            
            // Check token blacklist
            if (tokenBlacklistService.isTokenBlacklisted(authToken)) {
                logger.error("JWT token is blacklisted");
                return false;
            }
            
            // Add logging to see the token being validated
            logger.debug("Validating token: {}", authToken);
            
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            logger.error("JWT validation error: Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("JWT validation error: Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT validation error: JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT validation error: JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT validation error: JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }
    
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            // Extract token and handle potential extra spaces after "Bearer"
            String token = headerAuth.substring(6).trim();
            
            // Additional check for spaces within the actual token (not between Bearer and token)
            if (token.indexOf(' ') > 0) {
                logger.error("JWT token contains internal spaces");
                return null;
            }
            
            logger.debug("Extracted token: [{}]", token);
            return token;
        }
        return null;
    }

    // Method to extract token from request
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}