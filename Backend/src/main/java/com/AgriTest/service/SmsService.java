package com.AgriTest.service;

public interface SmsService {
    /**
     * Send an SMS message
     * 
     * @param phoneNumber Recipient phone number (international format)
     * @param message SMS message content
     * @return True if sent successfully, false otherwise
     */
    boolean sendSms(String phoneNumber, String message);
} 