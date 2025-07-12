package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String role;
    private Boolean enabled;
    private Boolean twoFactorEnabled;
    
    // Optional field - only processed if provided
    private String password;
    private String confirmPassword;
} 