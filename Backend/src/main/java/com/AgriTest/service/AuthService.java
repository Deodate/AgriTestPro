package com.AgriTest.service;

import com.AgriTest.dto.JwtResponse;
import com.AgriTest.dto.LoginRequest;
import com.AgriTest.dto.SignUpRequest;
import com.AgriTest.dto.UserResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    
    UserResponse registerUser(SignUpRequest signUpRequest);
}
