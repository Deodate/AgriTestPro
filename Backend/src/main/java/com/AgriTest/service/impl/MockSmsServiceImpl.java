package com.AgriTest.service.impl;

import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * A mock SMS service implementation for testing purposes.
 * This doesn't actually send SMS messages but logs them instead.
 * 
 * Activate this by adding the @Primary annotation when you don't want
 * to actually send SMS messages or when you're waiting for proper Infobip credentials.
 */
@Service
public class MockSmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(MockSmsServiceImpl.class);

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        logger.info("MOCK SMS SERVICE - Would send SMS to: {} with message: {}", phoneNumber, message);
        logger.info("******************************************");
        logger.info("* NOTE: This is a mock service - no real *");
        logger.info("* SMS messages are being sent. Use this  *");
        logger.info("* for testing when you don't have proper *");
        logger.info("* Infobip credentials configured.        *");
        logger.info("******************************************");
        
        // Always return success since this is a mock service
        return true;
    }
} 