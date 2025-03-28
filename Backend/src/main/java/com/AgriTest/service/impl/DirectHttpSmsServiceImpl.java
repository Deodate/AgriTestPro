package com.AgriTest.service.impl;

import com.AgriTest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Primary
public class DirectHttpSmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(DirectHttpSmsServiceImpl.class);
    
    private final String apiKey;
    private final String baseUrl;
    private final String sender;
    private final String username;
    private final String password;

    public DirectHttpSmsServiceImpl(
            @Value("${infobip.api.key}") String apiKey,
            @Value("${infobip.base.url}") String baseUrl,
            @Value("${infobip.sender}") String sender,
            @Value("${infobip.username:}") String username,
            @Value("${infobip.password:}") String password) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.sender = sender;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean sendSms(String phoneNumber, String message) {
        // Try multiple authentication methods, starting with the most likely to work
        
        // Try with Authorization: App {apiKey}
        if (trySendSmsWithAuth(phoneNumber, message, "App " + apiKey)) {
            return true;
        }
        
        // Try with Authorization: Bearer {apiKey}
        if (trySendSmsWithAuth(phoneNumber, message, "Bearer " + apiKey)) {
            return true;
        }
        
        // Try with plain API key in Authorization header
        if (trySendSmsWithAuth(phoneNumber, message, apiKey)) {
            return true;
        }
        
        // Try with API key in Api-Key header
        try {
            logger.debug("Trying with Api-Key header");
            
            // Prepare JSON request body
            String jsonBody = String.format(
                "{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}",
                sender, phoneNumber, message);
            
            // Create connection
            URL url = new URL(baseUrl + "/sms/2/text/advanced");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Api-Key", apiKey);
            
            // Enable input/output
            conn.setDoOutput(true);
            
            // Write request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Get response
            int responseCode = conn.getResponseCode();
            
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(responseCode >= 300 ? conn.getErrorStream() : conn.getInputStream(), 
                            StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            
            if (responseCode >= 200 && responseCode < 300) {
                logger.info("SMS sent successfully to {} with Api-Key header", phoneNumber);
                return true;
            } else {
                logger.debug("Api-Key header method failed: {} - {}", responseCode, response.toString());
            }
        } catch (Exception e) {
            logger.debug("Api-Key header method failed with exception: {}", e.getMessage());
        }
        
        // If username and password are provided, try Basic authentication
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            try {
                logger.debug("Trying with Basic authentication");
                
                // Prepare JSON request body
                String jsonBody = String.format(
                    "{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}",
                    sender, phoneNumber, message);
                
                // Create connection
                URL url = new URL(baseUrl + "/sms/2/text/advanced");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                
                // Set Basic authorization header
                String auth = username + ":" + password;
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
                
                // Enable input/output
                conn.setDoOutput(true);
                
                // Write request body
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                
                // Get response
                int responseCode = conn.getResponseCode();
                
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(responseCode >= 300 ? conn.getErrorStream() : conn.getInputStream(), 
                                StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }
                
                if (responseCode >= 200 && responseCode < 300) {
                    logger.info("SMS sent successfully to {} with Basic auth", phoneNumber);
                    return true;
                } else {
                    logger.debug("Basic auth method failed: {} - {}", responseCode, response.toString());
                }
            } catch (Exception e) {
                logger.debug("Basic auth method failed with exception: {}", e.getMessage());
            }
        }
        
        // If all methods failed, log an error and return false
        logger.error("All authentication methods failed when sending SMS to {}", phoneNumber);
        return false;
    }
    
    /**
     * Helper method to try sending SMS with a specific authorization header
     */
    private boolean trySendSmsWithAuth(String phoneNumber, String message, String authHeaderValue) {
        try {
            logger.debug("Trying with Authorization: {}", authHeaderValue);
            
            // Using simpler SMS API endpoint for testing
            String jsonBody = String.format(
                "{\"from\":\"%s\",\"to\":\"%s\",\"text\":\"%s\"}",
                sender, phoneNumber, message);
            
            // Create connection
            URL url = new URL(baseUrl + "/sms/1/text/single");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", authHeaderValue);
            
            // Enable input/output
            conn.setDoOutput(true);
            
            // Write request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            logger.debug("Full API URL: {}", url.toString());
            logger.debug("Request body: {}", jsonBody);
            
            // Get response
            int responseCode = conn.getResponseCode();
            
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(responseCode >= 300 ? conn.getErrorStream() : conn.getInputStream(), 
                            StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            
            logger.debug("Response code: {}", responseCode);
            logger.debug("Response body: {}", response.toString());
            
            if (responseCode >= 200 && responseCode < 300) {
                logger.info("SMS sent successfully to {}", phoneNumber);
                return true;
            } else {
                logger.debug("Auth method failed: {} - {}", responseCode, response.toString());
                return false;
            }
        } catch (Exception e) {
            logger.debug("Auth method failed with exception: {}", e.getMessage());
            return false;
        }
    }
} 