// src/main/java/com/AgriTest/service/TokenBlacklistService.java
package com.AgriTest.service;

public interface TokenBlacklistService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
    void cleanupExpiredTokens();
}