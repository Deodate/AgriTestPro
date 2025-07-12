package com.AgriTest.service;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.TwoFactorResponse;
import com.AgriTest.dto.TwoFactorVerificationRequest;
import com.AgriTest.dto.UserResponse;

public interface TwoFactorAuthService {

    /**
     * Generate a 2FA code for a user and send it via SMS
     * 
     * @param userId The user's ID
     * @return true if code was generated and sent, false otherwise
     */
    boolean generateAndSendCode(Long userId);
    
    /**
     * Verify a 2FA code entered by a user
     * 
     * @param userId The user's ID
     * @param code The code entered by the user
     * @return true if code is valid, false otherwise
     */
    boolean verifyCode(Long userId, String code);
    
    /**
     * Verify a 2FA code with more details
     * 
     * @param request The verification request containing user ID and code
     * @return The JWT token if verification is successful
     */
    String verifyCodeAndGenerateToken(TwoFactorVerificationRequest request);
    
    /**
     * Enable or disable 2FA for a user
     * 
     * @param userId The user's ID
     * @param enabled Whether to enable (true) or disable (false) 2FA
     * @return The updated user
     */
    UserResponse toggleTwoFactorAuth(Long userId, boolean enabled);
    
    /**
     * Enable or disable 2FA for a user by username
     * 
     * @param username The username
     * @param enabled Whether to enable (true) or disable (false) 2FA
     * @return The updated user
     */
    UserResponse toggleTwoFactorAuthByUsername(String username, boolean enabled);
    
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