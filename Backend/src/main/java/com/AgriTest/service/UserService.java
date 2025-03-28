package com.AgriTest.service;

import com.AgriTest.dto.UserResponse;
import com.AgriTest.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> getAllUsers();
    
    Optional<UserResponse> getUserById(Long id);
    
    Optional<UserResponse> getUserByUsername(String username);
    
    UserResponse createUser(User user);
    
    UserResponse updateUser(Long id, User user);
    
    void deleteUser(Long id);
    
    List<UserResponse> getUsersByRole(String role);
    
    /**
     * Maps a User entity to a UserResponse DTO
     * @param user The user entity to map
     * @return The corresponding UserResponse DTO
     */
    UserResponse mapUserToUserResponse(User user);
}