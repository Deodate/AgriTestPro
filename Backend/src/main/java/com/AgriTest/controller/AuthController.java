package com.AgriTest.controller;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.LoginRequest;
import com.AgriTest.dto.SignUpRequest;
import com.AgriTest.dto.TwoFactorResponse;
import com.AgriTest.dto.UserResponse;
import com.AgriTest.dto.ForgotPasswordRequest;
import com.AgriTest.dto.ResetPasswordRequest;
import com.AgriTest.dto.VerifyResetCodeRequest;
import com.AgriTest.model.User;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.security.jwt.JwtUtils;
import com.AgriTest.security.service.UserDetailsImpl;
import com.AgriTest.service.AuthService;
import com.AgriTest.service.TokenBlacklistService;
import com.AgriTest.service.UserService;
import com.AgriTest.service.TwoFactorAuthService;
import com.AgriTest.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Date;

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

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private PasswordResetService passwordResetService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        logger.error("Validation error: {}", errors);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");
        response.put("errors", errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login attempt for user: {}", loginRequest.getUsername());

            // First check if the user exists in the database
            Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
            if (userOptional.isEmpty()) {
                logger.warn("Login attempt for non-existent user: {}", loginRequest.getUsername());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
                errorResponse.put("message", "Username not found. Please check your username or register a new account.");
                errorResponse.put("error", "user_not_found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            User user = userOptional.get();
            
            // Check if the account is enabled
            if (!user.getEnabled()) {
                logger.warn("Login attempt for disabled account: {}", loginRequest.getUsername());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.FORBIDDEN.value());
                errorResponse.put("message", "Account is not activated. Please contact administration.");
                errorResponse.put("error", "account_disabled");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            // Now attempt authentication
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()));
            } catch (BadCredentialsException e) {
                logger.warn("Bad credentials for user: {}", loginRequest.getUsername());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
                errorResponse.put("message", "Invalid password. Please try again.");
                errorResponse.put("error", "invalid_password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate token for all cases
            String jwt;
            try {
                jwt = jwtUtils.generateJwtToken(authentication);
            } catch (Exception e) {
                logger.error("JWT token generation error: {}", e.getMessage());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                errorResponse.put("message", "Authentication error: Unable to generate security token.");
                errorResponse.put("error", "token_generation_failed");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            
            // Check if 2FA is enabled
            if (user.getTwoFactorEnabled() != null && user.getTwoFactorEnabled()) {
                // Generate and send 2FA code
                boolean codeSent = twoFactorAuthService.generateAndSendCode(user.getId());
                
                if (!codeSent) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "message", "Failed to send verification code",
                        "error", "verification_code_sending_failed"
                    ));
                }
                
                // Return response with both 2FA requirements and token
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Please verify with 2FA code sent to your mobile phone");
                response.put("userId", user.getId());
                response.put("username", user.getUsername());
                response.put("email", user.getEmail());
                response.put("requiresVerification", true);
                response.put("token", jwt); // Include the token
                response.put("roles", roles);
                response.put("tokenStatus", "temporary"); // Indicate token needs verification
                
                return ResponseEntity.ok(response);
            }

            // If 2FA not enabled, return normal response
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));

        } catch (Exception e) {
            logger.error("Authentication error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            errorResponse.put("error", "authentication_error");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            logger.info("Registration attempt for user: {}", signUpRequest.getUsername());

            // Set default full name if not provided
            if (signUpRequest.getFullName() == null || signUpRequest.getFullName().trim().isEmpty()) {
                signUpRequest.setFullName(signUpRequest.getUsername());
            }
            
            // Set default role if not provided
            if (signUpRequest.getRole() == null || signUpRequest.getRole().trim().isEmpty()) {
                signUpRequest.setRole("ROLE_USER");
            }
            
            // Validate password confirmation
            if (signUpRequest.getConfirmPassword() == null || !signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "Error: Passwords do not match!"));
            }

            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "Error: Email is already in use!"));
            }

            if (signUpRequest.getPhoneNumber() != null &&
                    userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "Error: Phone number is already in use!"));
            }
            
            // Validate role and convert to proper format
            String role = signUpRequest.getRole().toUpperCase().trim();
            
            // Standardize role format: convert spaces to underscores and add ROLE_ prefix if needed
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role.replace(" ", "_");
            }
            
            // List of allowed roles
            Set<String> allowedRoles = Set.of(
                "ROLE_ADMIN", 
                "ROLE_USER",
                "ROLE_AGRONOMIST", 
                "ROLE_STOREKEEPER", 
                "ROLE_MANUFACTURER", 
                "ROLE_FIELD_WORKER"
            );
            
            if (!allowedRoles.contains(role)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "Error: Invalid role specified. Valid roles are: User, Admin, Agronomist, Storekeeper, Manufacturer, Field Worker"));
            }
            
            // Set the standardized role
            signUpRequest.setRole(role);

            // Create new user's account
            User user = new User();
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setFullName(signUpRequest.getFullName());
            user.setPhoneNumber(signUpRequest.getPhoneNumber());
            user.setRole(signUpRequest.getRole());
            user.setEnabled(true); // Set to true for immediate access
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

    @PutMapping("/2fa/{userIdentifier}")
    // Removing @PreAuthorize annotation to allow public access
    public ResponseEntity<?> toggleTwoFactorAuth(
            @PathVariable String userIdentifier,
            @RequestParam boolean enabled) {
        try {
            UserResponse userResponse;

            // Check if the identifier is numeric (userId) or alphanumeric (username)
            if (userIdentifier.matches("\\d+")) {
                // It's a userId
                Long userId = Long.valueOf(userIdentifier);
                userResponse = authService.toggleTwoFactorAuth(userId, enabled);
            } else {
                // It's a username
                userResponse = authService.toggleTwoFactorAuthByUsername(userIdentifier, enabled);
            }

            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            logger.error("2FA toggle error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/signout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> signoutUser(HttpServletRequest request) {
        try {
            // Extract token from request
            String token = jwtUtils.extractTokenFromRequest(request);
            
            if (token == null || token.isEmpty()) {
                logger.warn("Logout attempt with no token");
                return ResponseEntity.badRequest().body(Map.of(
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "No valid token found in request"));
            }

            // Get the current authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "unknown";

            // Blacklist the token
            tokenBlacklistService.blacklistToken(token);
            logger.info("Token blacklisted for user: {}", username);
            
            // Clear the security context
            SecurityContextHolder.clearContext();
            
            // Return a success response with token info (masked for security)
            String maskedToken = token.substring(0, Math.min(10, token.length())) + "...";
            return ResponseEntity.ok(Map.of(
                    "status", HttpStatus.OK.value(),
                    "message", "You've been signed out successfully!",
                    "details", Map.of(
                        "username", username,
                        "tokenInvalidated", true,
                        "tokenInfo", maskedToken
                    )));
        } catch (Exception e) {
            logger.error("Signout error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "message", "An error occurred during signout: " + e.getMessage()));
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        try {
            // Extract token from request
            String token = jwtUtils.extractTokenFromRequest(request);

            if (token == null) {
                logger.error("No token provided for validation");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "valid", false,
                        "message", "No token provided"));
            }

            // Check if token is blacklisted
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                logger.error("Token is blacklisted");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "valid", false,
                        "message", "Token is invalid or has been revoked"));
            }

            // Validate the JWT token
            if (!jwtUtils.validateJwtToken(token)) {
                logger.error("Token validation failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "valid", false,
                        "message", "Token is invalid or has expired"));
            }

            // Extract username from token
            String username = jwtUtils.getUserNameFromJwtToken(token);

            // Check if the user exists in the database
            if (!userRepository.existsByUsername(username)) {
                logger.error("User not found for token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "status", HttpStatus.UNAUTHORIZED.value(),
                        "valid", false,
                        "message", "User not found"));
            }

            // If all checks pass, return success response
            logger.info("Token validated successfully for user: {}", username);
            return ResponseEntity.ok(Map.of(
                    "status", HttpStatus.OK.value(),
                    "valid", true,
                    "username", username,
                    "message", "Token is valid"));

        } catch (Exception e) {
            logger.error("Token validation error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "valid", false,
                    "message", "Error validating token: " + e.getMessage()));
        }
    }

    /**
     * Activates or deactivates a user account. Only accessible to administrators.
     * 
     * @param userIdentifier The user ID or username
     * @param enabled Whether to enable (true) or disable (false) the account
     * @return The updated user details
     */
    @PutMapping("/account-status/{userIdentifier}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAccountStatus(
            @PathVariable String userIdentifier,
            @RequestParam boolean enabled) {
        try {
            logger.info("Account status update requested for user: {}, enabled: {}", userIdentifier, enabled);
            UserResponse userResponse;

            // Check if the identifier is numeric (userId) or alphanumeric (username)
            if (userIdentifier.matches("\\d+")) {
                // It's a userId
                Long userId = Long.valueOf(userIdentifier);
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                
                user.setEnabled(enabled);
                User updatedUser = userRepository.save(user);
                userResponse = userService.mapUserToUserResponse(updatedUser);
                
                logger.info("Account status updated for user ID {}: enabled = {}", userId, enabled);
            } else {
                // It's a username
                User user = userRepository.findByUsername(userIdentifier)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + userIdentifier));
                
                user.setEnabled(enabled);
                User updatedUser = userRepository.save(user);
                userResponse = userService.mapUserToUserResponse(updatedUser);
                
                logger.info("Account status updated for username {}: enabled = {}", userIdentifier, enabled);
            }

            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            logger.error("Account status update error: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            logger.info("Password reset requested for email: {}", request.getEmail());
            
            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                // Return a generic error message for security reasons
                // This prevents user enumeration attacks by not confirming if an email exists
                logger.warn("Password reset attempted for non-existent email: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", HttpStatus.NOT_FOUND.value(),
                    "message", "The email address provided is not registered in our system. Please check your email or register for an account.",
                    "error", "email_not_found"
                ));
            }
            User user = userOptional.get();

            // Generate and send reset code via SMS
            String code = passwordResetService.generateResetCode(user);
            
            // Mask the phone number to show only the last 3 digits
            String phoneNumber = user.getPhoneNumber();
            String lastThreeDigits = phoneNumber.substring(Math.max(0, phoneNumber.length() - 3));
            
            // Create response with both message and debug information
            Map<String, Object> response = new HashMap<>();
            response.put("message", String.format("A reset code has been sent to your phone number ending in %s. The code will expire in 60 seconds.", lastThreeDigits));
            response.put("status", HttpStatus.OK.value());
            response.put("debug", Map.of(
                "resetCode", code,
                "phoneNumber", phoneNumber
            ));
            
            logger.info("Reset code generated: {} for phone number: {}", code, phoneNumber);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Forgot password error: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            logger.info("Password reset attempt with code");

            // Validate password confirmation
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "message", "Passwords do not match"
                ));
            }

            // Validate code and reset password
            boolean success = passwordResetService.resetPassword(request.getToken(), request.getNewPassword());

            if (!success) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "message", "Invalid or expired reset code"
                ));
            }

            return ResponseEntity.ok(Map.of(
                "status", HttpStatus.OK.value(),
                "message", "Password has been reset successfully"
            ));

        } catch (Exception e) {
            logger.error("Reset password error: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@Valid @RequestBody VerifyResetCodeRequest request) {
        try {
            logger.info("Verifying reset code and setting new password");

            // Validate password confirmation
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "message", "Passwords do not match"
                ));
            }

            // Validate code and reset password
            boolean success = passwordResetService.resetPassword(request.getCode(), request.getNewPassword());

            if (!success) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", HttpStatus.BAD_REQUEST.value(),
                    "message", "Invalid or expired reset code"
                ));
            }

            return ResponseEntity.ok(Map.of(
                "status", HttpStatus.OK.value(),
                "message", "Password has been reset successfully"
            ));

        } catch (Exception e) {
            logger.error("Reset code verification error: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        logger.info("Auth system health check");
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "Authentication system is healthy",
            "timestamp", new Date()
        ));
    }
}