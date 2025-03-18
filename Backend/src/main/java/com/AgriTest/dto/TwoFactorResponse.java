package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorResponse {
    private String message;
    private Long userId;
    private String username;
    private boolean requiresVerification;
    private Long expiresAt;
    private String generatedCode; // New field for testing
}