package com.AgriTest.controller;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.TwoFactorVerificationRequest;
import com.AgriTest.model.User;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.security.service.UserDetailsImpl;
import com.AgriTest.service.TwoFactorAuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/2fa")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TwoFactorAuthController {

    private static final Logger logger = LoggerFactory.getLogger(TwoFactorAuthController.class);

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Generate and send a 2FA code for the specified user
     *
     * @param userId User ID
     * @return Response entity with success/failure status
     */
    @PostMapping("/generate/{userId}")
    public ResponseEntity<?> generateCode(@PathVariable Long userId) {
        if (twoFactorAuthService.generateAndSendCode(userId)) {
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Verification code sent successfully"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Failed to generate verification code"
            ));
        }
    }

    /**
     * Verify a 2FA code and generate a JWT token if valid
     *
     * @param request Verification request containing userId and code
     * @return Response entity with JWT token or error
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody TwoFactorVerificationRequest request) {
        try {
            String token = twoFactorAuthService.verifyCodeAndGenerateToken(request);
            
            if (token != null) {
                // Fetch user details to include in response
                User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                // Create a user details object for constructing the response
                UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                
                // Get roles
                List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
                
                // Return JWT token with user info
                return ResponseEntity.ok(new JwtResponse(
                    token,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Invalid verification code"
                ));
            }
        } catch (Exception e) {
            logger.error("Error verifying 2FA code: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", "Error verifying code: " + e.getMessage()
            ));
        }
    }
}