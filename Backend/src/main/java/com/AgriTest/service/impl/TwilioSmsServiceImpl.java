package com.AgriTest.service.impl;

import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
@ConditionalOnProperty(name = "twilio.enabled", havingValue = "true", matchIfMissing = false)
public class TwilioSmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioSmsServiceImpl.class);
    
    @Value("${twilio.account.sid}")
    private String accountSid;
    
    @Value("${twilio.auth.token}")
    private String authToken;
    
    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;
    
    @Override
    public boolean sendSms(String phoneNumber, String message) {
        try {
            if ("disabled".equals(accountSid) || "disabled".equals(authToken)) {
                logger.warn("Twilio is not properly configured. SMS will not be sent.");
                return false;
            }
            
            // Initialize Twilio client with credentials
            Twilio.init(accountSid, authToken);
            
            // Send SMS
            Message twilioMessage = Message.creator(
                new PhoneNumber(phoneNumber),  // To 
                new PhoneNumber(twilioPhoneNumber),  // From
                message
            ).create();
            
            logger.info("SMS sent with SID: {}", twilioMessage.getSid());
            return true;
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
            return false;
        }
    }
} 