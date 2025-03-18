package com.AgriTest.service;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.LoginRequest;
import com.AgriTest.dto.SignUpRequest;
import com.AgriTest.dto.TwoFactorResponse;
import com.AgriTest.dto.UserResponse;

public interface AuthService {
    /**
     * Authenticate a user with username and password
     * If 2FA is enabled, will return a partial authentication requiring verification
     */
    TwoFactorResponse authenticateUser(LoginRequest loginRequest);
    
    /**
     * Register a new user
     */
    UserResponse registerUser(SignUpRequest signUpRequest);
    
    /**
     * Enable or disable two-factor authentication for a user
     */
    UserResponse toggleTwoFactorAuth(Long userId, boolean enabled);
}