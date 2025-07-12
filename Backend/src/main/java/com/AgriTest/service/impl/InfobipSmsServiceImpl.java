package com.AgriTest.service.impl;

import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

@Service
@Primary
public class InfobipSmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(InfobipSmsServiceImpl.class);
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;
    private final String sender;

    public InfobipSmsServiceImpl(
            @Value("${infobip.api.key}") String apiKey,
            @Value("${infobip.base.url}") String baseUrl,
            @Value("${infobip.sender}") String sender,
            RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.sender = sender;
        this.restTemplate = restTemplate;
        
        logger.info("Initializing Infobip SMS service with base URL: {}", baseUrl);
    }

    private String formatPhoneNumber(String phoneNumber) {
        // Remove any spaces, dashes, or parentheses
        String cleaned = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
        
        // Remove leading zeros
        cleaned = cleaned.replaceAll("^0+", "");
        
        // If number starts with +, keep it, otherwise add +250 (Rwanda)
        if (!cleaned.startsWith("+")) {
            cleaned = "+250" + cleaned;
        }
        
        logger.debug("Formatted phone number from {} to {}", phoneNumber, cleaned);
        return cleaned;
    }

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        try {
            String formattedNumber = formatPhoneNumber(phoneNumber);
            logger.debug("Preparing to send SMS to {} (formatted from {}) with message: {}", 
                formattedNumber, phoneNumber, message);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "App " + apiKey);
            
            logger.debug("Using Infobip base URL: {}", baseUrl);
            logger.debug("Using sender: {}", sender);

            // Using simpler message format
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("from", sender);
            requestBody.put("to", formattedNumber);
            requestBody.put("text", message);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String url = baseUrl + "/sms/2/text/single";
            
            logger.debug("Full API URL: {}", url);
            logger.debug("Request headers: {}", headers);
            logger.debug("Request body: {}", requestBody);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            logger.debug("Received response from Infobip: status={}, body={}", 
                    response.getStatusCode(), response.getBody());
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SMS sent successfully to {}", formattedNumber);
                return true;
            } else {
                logger.error("Failed to send SMS to {}: {}", formattedNumber, response.getBody());
                return false;
            }
        } catch (Exception e) {
            logger.error("Exception while sending SMS to {}: {}", phoneNumber, e.getMessage(), e);
            return false;
        }
    }
} 