// src/main/java/com/AgriTest/service/impl/TokenBlacklistServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.service.TokenBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistServiceImpl.class);
    
    // Map to store blacklisted tokens with their expiration time
    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    // Default token blacklist duration (24 hours - longer than token validity)
    private static final long TOKEN_BLACKLIST_DURATION = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    @Override
    public void blacklistToken(String token) {
        // Add token to blacklist with expiration time
        long expirationTime = Instant.now().toEpochMilli() + TOKEN_BLACKLIST_DURATION;
        blacklistedTokens.put(token, expirationTime);
        logger.debug("Token blacklisted, will expire at: {}", Instant.ofEpochMilli(expirationTime));
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        // Check if token exists and is not expired
        Long expirationTime = blacklistedTokens.get(token);
        if (expirationTime == null) {
            return false;
        }

        // Remove if token is expired
        if (expirationTime < Instant.now().toEpochMilli()) {
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }

    @Override
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupExpiredTokens() {
        int beforeSize = blacklistedTokens.size();
        long currentTime = Instant.now().toEpochMilli();
        
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < currentTime);
        
        int afterSize = blacklistedTokens.size();
        int removedCount = beforeSize - afterSize;
        
        if (removedCount > 0) {
            logger.info("Cleaned up {} expired tokens from blacklist. Current size: {}", 
                       removedCount, afterSize);
        }
    }
}