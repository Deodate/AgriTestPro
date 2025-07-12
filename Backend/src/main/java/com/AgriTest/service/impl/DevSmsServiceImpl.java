package com.AgriTest.service.impl;

import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Development implementation of SmsService that doesn't actually send SMS
 * but logs the message for development and testing purposes.
 */
@Service
@Profile("dev")
public class DevSmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(DevSmsServiceImpl.class);

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        logger.info("DEV SMS SERVICE - Would send SMS to: {} with message: {}", phoneNumber, message);
        // Always return true as if the SMS was sent successfully
        return true;
    }
} 