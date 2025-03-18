// File: src/main/java/com/AgriTest/controller/AuthController.java
package com.AgriTest.controller;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.LoginRequest;
import com.AgriTest.dto.SignUpRequest;
import com.AgriTest.dto.TwoFactorResponse;
import com.AgriTest.dto.UserResponse;
import com.AgriTest.model.User;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.security.jwt.JwtUtils;
import com.AgriTest.security.service.UserDetailsImpl;
import com.AgriTest.service.AuthService;
import com.AgriTest.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for user: {}", loginRequest.getUsername());
            
            // Direct authentication approach
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            
            // Check if 2FA is enabled
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (user.getTwoFactorEnabled() != null && user.getTwoFactorEnabled()) {
                return ResponseEntity.ok(TwoFactorResponse.builder()
                        .message("Please verify with 2FA code")
                        .userId(user.getId())
                        .username(user.getUsername())
                        .requiresVerification(true)
                        .build());
            }

            // If 2FA not enabled, return token immediately
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles
            ));
            
        } catch (Exception e) {
            logger.error("Authentication error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            logger.info("Registration attempt for user: {}", signUpRequest.getUsername());
            
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "message", "Error: Username is already taken!"
                ));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "message", "Error: Email is already in use!"
                ));
            }
            
            if (signUpRequest.getPhoneNumber() != null && 
                userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "message", "Error: Phone number is already in use!"
                ));
            }

            // Create new user's account
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setFullName(signUpRequest.getFullName());
            user.setPhoneNumber(signUpRequest.getPhoneNumber());
            user.setRole(signUpRequest.getRole());
            user.setEnabled(true);
            user.setTwoFactorEnabled(false);

            UserResponse savedUser = userService.createUser(user);
            logger.info("User registered successfully: {}", savedUser.getUsername());
            
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("Registration error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PutMapping("/2fa/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUserId(#userId)")
    public ResponseEntity<?> toggleTwoFactorAuth(
            @PathVariable Long userId, 
            @RequestParam boolean enabled) {
        try {
            UserResponse userResponse = authService.toggleTwoFactorAuth(userId, enabled);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            logger.error("2FA toggle error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}