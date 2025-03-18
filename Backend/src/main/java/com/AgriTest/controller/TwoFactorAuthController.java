package com.AgriTest.controller;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.TwoFactorCodeRequest;
import com.AgriTest.dto.TwoFactorResponse;
import com.AgriTest.dto.TwoFactorVerifyRequest;
import com.AgriTest.service.TwoFactorAuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/2fa")
public class TwoFactorAuthController {
    private static final Logger logger = LoggerFactory.getLogger(TwoFactorAuthController.class);

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;
    
    /**
     * Request a 2FA code be sent to the phone number
     */
    @PostMapping("/request-code")
    public ResponseEntity<?> requestCode(
            @Valid @RequestBody TwoFactorCodeRequest request,
            @RequestParam String verificationKey) {
        
        try {
            logger.info("2FA code request for phone number: {}", request.getPhoneNumber());
            TwoFactorResponse response = twoFactorAuthService.generateCode(
                    request.getPhoneNumber(), verificationKey);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("2FA code request error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Verify a 2FA code and complete authentication
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(
            @Valid @RequestBody TwoFactorVerifyRequest request) {
        
        try {
            logger.info("2FA code verification for user: {}", request.getUsername());
            JwtResponse response = twoFactorAuthService.verifyCode(
                    request.getCode(), request.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("2FA code verification error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Refresh an expired 2FA code
     */
    @PostMapping("/refresh-code")
    public ResponseEntity<?> refreshCode(
            @Valid @RequestBody TwoFactorCodeRequest request) {
        
        try {
            logger.info("2FA code refresh for phone number: {}", request.getPhoneNumber());
            TwoFactorResponse response = twoFactorAuthService.refreshCode(
                    request.getPhoneNumber());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("2FA code refresh error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}