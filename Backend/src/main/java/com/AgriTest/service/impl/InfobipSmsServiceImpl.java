package com.AgriTest.service.impl;

import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
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
    }

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        try {
            logger.debug("Preparing to send SMS to {} with message: {}", phoneNumber, message);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", apiKey);
            
            logger.debug("Using Infobip base URL: {}", baseUrl);
            logger.debug("Using sender: {}", sender);

            Map<String, Object> destination = new HashMap<>();
            destination.put("to", phoneNumber);

            Map<String, Object> smsMessage = new HashMap<>();
            smsMessage.put("from", sender);
            smsMessage.put("destinations", new Object[]{destination});
            smsMessage.put("text", message);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", new Object[]{smsMessage});

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String url = baseUrl + "/sms/2/text/advanced";
            
            logger.debug("Full API URL: {}", url);
            logger.debug("Request headers: {}", headers);
            logger.debug("Request body: {}", requestBody);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            logger.debug("Received response from Infobip: status={}, body={}", 
                    response.getStatusCode(), response.getBody());
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SMS sent successfully to {}", phoneNumber);
                return true;
            } else {
                logger.error("Failed to send SMS to {}: {}", phoneNumber, response.getBody());
                return false;
            }
        } catch (Exception e) {
            logger.error("Exception while sending SMS to {}: {}", phoneNumber, e.getMessage(), e);
            return false;
        }
    }
} 