package com.AgriTest.service.impl;

import com.AgriTest.dto.LoginRequest;
import com.AgriTest.dto.SignUpRequest;
import com.AgriTest.dto.TwoFactorResponse;
import com.AgriTest.dto.UserResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.User;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.security.jwt.JwtUtils;
import com.AgriTest.service.AuthService;
import com.AgriTest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            UserService userService,
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TwoFactorResponse authenticateUser(LoginRequest loginRequest) {
        try {
            // Authenticate with username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Get user details
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + loginRequest.getUsername()));
            
            // Check if 2FA is enabled
            if (user.getTwoFactorEnabled()) {
                // Return response indicating 2FA is required
                return TwoFactorResponse.builder()
                        .message("Please verify with 2FA code")
                        .userId(user.getId())
                        .username(user.getUsername())
                        .requiresVerification(true)
                        .build();
            } else {
                // 2FA not required, generate JWT
                String jwt = jwtUtils.generateJwtToken(authentication);
                
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                
                // Return response with JWT token
                return TwoFactorResponse.builder()
                        .message("Authentication successful")
                        .userId(user.getId())
                        .username(user.getUsername())
                        .requiresVerification(false)
                        .build();
            }
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public UserResponse registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        
        if (signUpRequest.getPhoneNumber() != null && 
            userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new RuntimeException("Error: Phone number is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFullName(signUpRequest.getFullName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setRole(signUpRequest.getRole());
        user.setEnabled(true);
        user.setTwoFactorEnabled(false); // Default to 2FA disabled

        return userService.createUser(user);
    }
    
    @Override
    public UserResponse toggleTwoFactorAuth(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.setTwoFactorEnabled(enabled);
        User updatedUser = userRepository.save(user);
        
        return userService.mapUserToUserResponse(updatedUser);
    }
    
    @Override
    public UserResponse toggleTwoFactorAuthByUsername(String username, boolean enabled) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        user.setTwoFactorEnabled(enabled);
        User updatedUser = userRepository.save(user);
        
        return userService.mapUserToUserResponse(updatedUser);
    }
}