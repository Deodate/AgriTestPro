package com.AgriTest.service.impl;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.TwoFactorResponse;
import com.AgriTest.model.TwoFactorCode;
import com.AgriTest.model.User;
import com.AgriTest.repository.TwoFactorCodeRepository;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.security.jwt.JwtUtils;
import com.AgriTest.security.service.UserDetailsServiceImpl;
import com.AgriTest.service.TwoFactorAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

    private static final Logger logger = LoggerFactory.getLogger(TwoFactorAuthServiceImpl.class);
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String VERIFICATION_KEY = "W23U";
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTES = 2;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TwoFactorCodeRepository twoFactorCodeRepository;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    @Transactional
    public TwoFactorResponse generateCode(String phoneNumber, String verificationKey) {
        // Validate verification key
        if (!VERIFICATION_KEY.equals(verificationKey)) {
            logger.error("Invalid verification key: {}. Expected: {}", verificationKey, VERIFICATION_KEY);
            throw new RuntimeException("Invalid verification key");
        }
        
        // Find user by phone number with detailed logging
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    logger.error("No user found with phone number: {}", phoneNumber);
                    return new RuntimeException("User not found with given phone number");
                });
        
        // Detailed user validation logging
        logger.info("User Details:");
        logger.info("ID: {}", user.getId());
        logger.info("Username: {}", user.getUsername());
        logger.info("Phone Number: {}", user.getPhoneNumber());
        logger.info("Two-Factor Enabled: {}", user.getTwoFactorEnabled());
        
        // Validate 2FA is enabled with more comprehensive check
        if (user.getTwoFactorEnabled() == null) {
            logger.warn("Two-factor authentication flag is NULL for user: {}", user.getUsername());
            throw new RuntimeException("Two-factor authentication status is not configured");
        }
        
        if (!user.getTwoFactorEnabled()) {
            logger.warn("Two-factor authentication is disabled for user: {}", user.getUsername());
            throw new RuntimeException("Two-factor authentication is not enabled");
        }
        
        // Generate verification and temporary codes
        String verificationCode = generateRandomCode(CODE_LENGTH);
        String temporaryCode = generateRandomCode(CODE_LENGTH);
        
        // Set expiration time with precise logging
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(CODE_EXPIRATION_MINUTES);
        
        logger.info("Code Generation Timestamps:");
        logger.info("Current Time: {}", now);
        logger.info("Expiration Time: {}", expiresAt);
        
        // Create and save 2FA code
        TwoFactorCode twoFactorCode = new TwoFactorCode(
            user, 
            verificationCode, 
            phoneNumber, 
            expiresAt,
            temporaryCode
        );
        
        // Save to database with error handling
        try {
            TwoFactorCode savedCode = twoFactorCodeRepository.save(twoFactorCode);
            logger.info("2FA Code Saved - ID: {}", savedCode.getId());
        } catch (Exception e) {
            logger.error("Failed to save 2FA code", e);
            throw new RuntimeException("Failed to generate 2FA code", e);
        }
        
        // Detailed logging of generated codes
        logger.info("2FA Code Generated:");
        logger.info("Verification Code: {}", verificationCode);
        logger.info("Temporary Code: {}", temporaryCode);
        
        return TwoFactorResponse.builder()
                .message("2FA code generated")
                .userId(user.getId())
                .username(user.getUsername())
                .requiresVerification(true)
                .expiresAt(expiresAt.toEpochSecond(java.time.ZoneOffset.UTC))
                .generatedCode(temporaryCode)
                .build();
    }

    @Override
    @Transactional
    public JwtResponse verifyCode(String code, String username) {
        // Find the user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Fetch all active, unused codes for this user
        List<TwoFactorCode> activeCodes = twoFactorCodeRepository
                .findByUserAndUsedFalseAndExpiresAtAfter(user, LocalDateTime.now());
        
        // Detailed logging
        logger.info("Verification Attempt Details:");
        logger.info("Username: {}", username);
        logger.info("Attempted Code: {}", code);
        logger.info("Active Codes Count: {}", activeCodes.size());
        
        // Check if any active codes match the provided code
        Optional<TwoFactorCode> matchingCodeOpt = activeCodes.stream()
                .filter(twoFactorCode -> 
                    (twoFactorCode.getCode() != null && twoFactorCode.getCode().equals(code)) || 
                    (twoFactorCode.getTemporaryCode() != null && twoFactorCode.getTemporaryCode().equals(code)))
                .findFirst();
        
        // If no matching code found
        if (matchingCodeOpt.isEmpty()) {
            // Log details of active codes
            activeCodes.forEach(activeCode -> 
                logger.warn("Active Code - Code: {}, Temp Code: {}, Expires At: {}", 
                    activeCode.getCode(), 
                    activeCode.getTemporaryCode(), 
                    activeCode.getExpiresAt())
            );
            
            throw new RuntimeException("Invalid or expired code");
        }
        
        // Get the matching code
        TwoFactorCode twoFactorCode = matchingCodeOpt.get();
        
        // Mark the code as used
        twoFactorCode.setUsed(true);
        twoFactorCodeRepository.save(twoFactorCode);
        
        logger.info("2FA code verified successfully for user: {}", username);
        
        // Create authentication and generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // Create and return JwtResponse
        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public TwoFactorResponse refreshCode(String phoneNumber) {
        // Find user by phone number
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Phone number not found"));
        
        // Check for existing active codes and invalidate them
        List<TwoFactorCode> activeCodes = twoFactorCodeRepository
                .findByUserAndUsedFalseAndExpiresAtAfter(user, LocalDateTime.now());
        
        activeCodes.forEach(code -> {
            code.setUsed(true);
            twoFactorCodeRepository.save(code);
        });
        
        // Generate a new verification code
        String verificationCode = generateRandomCode(CODE_LENGTH);
        
        // Generate another 6-digit code to be returned temporarily
        String temporaryCode = generateRandomCode(CODE_LENGTH);
        
        // Calculate expiration time (2 minutes from now)
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);
        
        // Save the new code to the database
        TwoFactorCode twoFactorCode = new TwoFactorCode(
            user, 
            verificationCode, 
            phoneNumber, 
            expiresAt,
            temporaryCode
        );
        twoFactorCodeRepository.save(twoFactorCode);
        
        // Log the codes for testing/development
        logger.info("Refreshed verification code for user {}: {}", user.getUsername(), verificationCode);
        logger.info("Refreshed temporary code for user {}: {}", user.getUsername(), temporaryCode);
        
        return TwoFactorResponse.builder()
                .message("New 2FA code generated")
                .userId(user.getId())
                .username(user.getUsername())
                .requiresVerification(true)
                .expiresAt(expiresAt.toEpochSecond(java.time.ZoneOffset.UTC))
                .generatedCode(temporaryCode)
                .build();
    }

    @Override
    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @Transactional
    public void cleanupExpiredCodes() {
        logger.info("Cleaning up expired 2FA codes");
        twoFactorCodeRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
    
    /**
     * Generate a random numeric code of the specified length
     */
    private String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(RANDOM.nextInt(10)); // Generates a digit between 0-9
        }
        return code.toString();
    }
}