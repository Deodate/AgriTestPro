package com.AgriTest.service;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.TwoFactorResponse;

public interface TwoFactorAuthService {
    
    /**
     * Generate a two-factor authentication code for a user with the given phone number
     * @param phoneNumber The phone number to send the code to
     * @param verificationKey The verification key for requesting a code
     * @return A response containing the status and expiration time
     */
    TwoFactorResponse generateCode(String phoneNumber, String verificationKey);
    
    /**
     * Verify a two-factor authentication code
     * @param code The code to verify
     * @param username The username associated with the code
     * @return A JWT response if verification is successful
     */
    JwtResponse verifyCode(String code, String username);
    
    /**
     * Refresh an expired two-factor authentication code
     * @param phoneNumber The phone number to send the new code to
     * @return A response containing the status and expiration time
     */
    TwoFactorResponse refreshCode(String phoneNumber);
    
    /**
     * Clean up expired codes from the database
     */
    void cleanupExpiredCodes();
}