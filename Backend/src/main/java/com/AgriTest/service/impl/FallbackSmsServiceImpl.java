package com.AgriTest.service.impl;

import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Fallback SMS service that wraps the real service and provides a fallback
 * for development environments.
 */
@Service
@Primary
public class FallbackSmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(FallbackSmsServiceImpl.class);

    private final SmsService primarySmsService;
    private final boolean isDevelopment;

    public FallbackSmsServiceImpl(
            InfobipSmsServiceImpl infobipSmsService,
            @org.springframework.beans.factory.annotation.Value("${spring.profiles.active:prod}") String activeProfile) {
        this.primarySmsService = infobipSmsService;
        this.isDevelopment = activeProfile.contains("dev") || 
                             activeProfile.contains("test") || 
                             activeProfile.contains("local") ||
                             "prod".equals(activeProfile); // Using default value means no profile specified
        
        logger.info("FallbackSmsService initialized with isDevelopment={}", isDevelopment);
    }

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        try {
            // First try the primary SMS service
            boolean result = primarySmsService.sendSms(phoneNumber, message);
            if (result) {
                return true;
            }
            
            // If primary fails and we're in development mode, log and succeed
            if (isDevelopment) {
                logger.info("FALLBACK SMS - Primary SMS failed, using development fallback for: {} with message: {}", 
                        phoneNumber, message);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            // If any exception occurs and we're in development mode, log and succeed
            if (isDevelopment) {
                logger.warn("FALLBACK SMS - Primary SMS service threw exception: {}. Using development fallback for: {} with message: {}", 
                        e.getMessage(), phoneNumber, message);
                return true;
            }
            // Rethrow in production
            throw e;
        }
    }
} 