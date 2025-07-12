// File: src/main/java/com/AgriTest/dto/TwoFactorVerifyRequest.java
package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorVerifyRequest {
    @NotBlank(message = "2FA code is required")
    private String code;
    
    @NotBlank(message = "Username is required")
    private String username;
}