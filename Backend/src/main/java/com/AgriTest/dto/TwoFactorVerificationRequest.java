package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorVerificationRequest {
    private Long userId;
    private String username;
    private String code;
} 