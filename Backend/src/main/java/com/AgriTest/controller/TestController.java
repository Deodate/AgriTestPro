package com.AgriTest.controller;

import com.AgriTest.model.User;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.service.SmsService;
import com.AgriTest.service.TwoFactorAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private SmsService smsService;

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sms")
    public ResponseEntity<?> testSms(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone number is required"));
        }

        try {
            logger.info("Starting 2FA code generation process for phone number: {}", phoneNumber);
            
            // Find user by phone number instead of ID
            User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found with phone number: " + phoneNumber));
            logger.info("Found user: {}", user.getUsername());
            
            // Enable 2FA for the user
            logger.info("Enabling 2FA for user");
            twoFactorAuthService.toggleTwoFactorAuth(user.getId(), true);
            
            // Generate and send 2FA code
            logger.info("Generating and sending 2FA code");
            boolean success = twoFactorAuthService.generateAndSendCode(user.getId());
            
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "2FA code sent successfully");
                response.put("phoneNumber", phoneNumber);
                response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                response.put("smsServiceType", "DirectHttpSmsServiceImpl");
                logger.info("2FA code generation and sending completed successfully");
                return ResponseEntity.ok(response);
            } else {
                logger.error("Failed to generate and send 2FA code");
                return ResponseEntity.internalServerError().body(Map.of("error", "Failed to send 2FA code"));
            }
        } catch (Exception e) {
            logger.error("Error sending 2FA code: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to send 2FA code: " + e.getMessage()));
        }
    }

    @PostMapping("/sms/direct")
    public ResponseEntity<?> testDirectSms(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        String message = request.get("message");
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone number is required"));
        }
        
        if (message == null || message.trim().isEmpty()) {
            message = "This is a test SMS from AgriTest Pro";
        }

        try {
            logger.info("Testing direct SMS sending to phone number: {}", phoneNumber);
            boolean success = smsService.sendSms(phoneNumber, message);
            
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Test SMS sent successfully");
                response.put("phoneNumber", phoneNumber);
                response.put("testMessage", message);
                response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                logger.info("Test SMS sent successfully to {}", phoneNumber);
                return ResponseEntity.ok(response);
            } else {
                logger.error("Failed to send test SMS");
                return ResponseEntity.internalServerError().body(Map.of("error", "Failed to send test SMS"));
            }
        } catch (Exception e) {
            logger.error("Error sending test SMS: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
} 