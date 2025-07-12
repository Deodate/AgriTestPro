package com.AgriTest.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfobipSmsServiceImplTest {

    private InfobipSmsServiceImpl smsService;

    @Mock
    private RestTemplate restTemplate;

    private static final String TEST_API_KEY = "test_api_key";
    private static final String TEST_BASE_URL = "https://test.api.infobip.com";
    private static final String TEST_SENDER = "TestSender";

    @BeforeEach
    void setUp() {
        smsService = new InfobipSmsServiceImpl(TEST_API_KEY, TEST_BASE_URL, TEST_SENDER, restTemplate);
    }

    @Test
    void sendSms_Success() {
        // Arrange
        String phoneNumber = "0788123456";
        String message = "Test message";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        
        when(restTemplate.postForEntity(
            anyString(),
            any(),
            eq(Map.class)
        )).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        // Act
        boolean result = smsService.sendSms(phoneNumber, message);

        // Assert
        assertTrue(result);
        verify(restTemplate).postForEntity(
            eq(TEST_BASE_URL + "/sms/2/text/single"),
            any(),
            eq(Map.class)
        );
    }

    @Test
    void sendSms_Failure() {
        // Arrange
        String phoneNumber = "0788123456";
        String message = "Test message";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "error");
        
        when(restTemplate.postForEntity(
            anyString(),
            any(),
            eq(Map.class)
        )).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST));

        // Act
        boolean result = smsService.sendSms(phoneNumber, message);

        // Assert
        assertFalse(result);
        verify(restTemplate).postForEntity(
            eq(TEST_BASE_URL + "/sms/2/text/single"),
            any(),
            eq(Map.class)
        );
    }

    @Test
    void sendSms_PhoneNumberFormatting() {
        // Arrange
        String[] testNumbers = {
            "0788123456",      // Standard format
            "+250788123456",   // With country code
            "788123456",       // Without leading zero
            "078-812-3456",    // With dashes
            "(078) 812 3456"   // With spaces and parentheses
        };
        String message = "Test message";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", "success");
        
        when(restTemplate.postForEntity(
            anyString(),
            any(),
            eq(Map.class)
        )).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        // Act & Assert
        for (String number : testNumbers) {
            boolean result = smsService.sendSms(number, message);
            assertTrue(result, "Failed for number: " + number);
        }
    }
} 