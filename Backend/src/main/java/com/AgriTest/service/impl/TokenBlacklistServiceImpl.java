// src/main/java/com/AgriTest/service/impl/TokenBlacklistServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.service.TokenBlacklistService;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    // Map to store blacklisted tokens with their expiration time
    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    // Default token blacklist duration (1 hour)
    private static final long TOKEN_BLACKLIST_DURATION = 3600_000; // 1 hour in milliseconds

    @Override
    public void blacklistToken(String token) {
        // Add token to blacklist with expiration time
        blacklistedTokens.put(token, Instant.now().toEpochMilli() + TOKEN_BLACKLIST_DURATION);
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
    public void cleanupExpiredTokens() {
        long currentTime = Instant.now().toEpochMilli();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < currentTime);
    }
}