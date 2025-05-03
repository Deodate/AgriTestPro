package com.AgriTest.service;

public interface SmsService {
    /**
     * Send an SMS message to a phone number
     * 
     * @param phoneNumber The recipient's phone number
     * @param message The message to send
     * @return true if the message was sent successfully, false otherwise
     */
    boolean sendSms(String phoneNumber, String message);
} 