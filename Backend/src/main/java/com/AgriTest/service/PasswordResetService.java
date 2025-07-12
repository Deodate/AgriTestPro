package com.AgriTest.service;

import com.AgriTest.model.User;
import com.AgriTest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SmsService smsService;
    
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    // Token expiration time (1 minute)
    private static final long TOKEN_EXPIRATION_SECONDS = 60;
    private final Random random = new Random();

    public String generateResetCode(User user) {
        // Generate 4-digit code
        String code = String.format("%04d", random.nextInt(10000));
        user.setResetToken(code);
        user.setResetTokenExpiry(Instant.now().plusSeconds(TOKEN_EXPIRATION_SECONDS));
        
        // Send SMS with the reset code
        String message = String.format("Your password reset code is: %s. This code will expire in 60 seconds.", code);
        boolean smsSent = false;
        
        try {
            smsSent = smsService.sendSms(user.getPhoneNumber(), message);
        } catch (Exception e) {
            logger.error("SMS service error: {}", e.getMessage());
            // Continue with the code generation even if SMS fails
            smsSent = isDevelopmentMode();
        }
        
        if (!smsSent && !isDevelopmentMode()) {
            logger.error("Failed to send SMS to phone number: {}", user.getPhoneNumber());
            throw new RuntimeException("Failed to send reset code via SMS");
        } else if (!smsSent) {
            logger.warn("SMS sending failed, but continuing in development mode. Code: {}", code);
        }
        
        userRepository.save(user);
        return code;
    }

    public boolean validateResetCode(String code) {
        User user = userRepository.findByResetToken(code)
                .orElse(null);

        if (user == null) {
            return false;
        }

        // Check if code is expired
        if (user.getResetTokenExpiry().isBefore(Instant.now())) {
            // Clear expired code
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
            return false;
        }

        return true;
    }

    public boolean resetPassword(String code, String newPassword) {
        User user = userRepository.findByResetToken(code)
                .orElse(null);

        if (user == null || !validateResetCode(code)) {
            return false;
        }

        // Update password and clear reset code
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        // Send confirmation SMS
        try {
        String message = "Your password has been reset successfully.";
        smsService.sendSms(user.getPhoneNumber(), message);
        } catch (Exception e) {
            // Log the error but don't fail the password reset
            logger.error("Failed to send password reset confirmation SMS: {}", e.getMessage());
        }

        return true;
    }
    
    /**
     * Check if the application is running in development mode
     * 
     * @return true if in development mode
     */
    private boolean isDevelopmentMode() {
        return activeProfile.contains("dev") || activeProfile.contains("test") || activeProfile.contains("local");
    }
} 