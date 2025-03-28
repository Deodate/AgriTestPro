// File: src/main/java/com/AgriTest/controller/UserController.java
package com.AgriTest.controller;

import com.AgriTest.dto.UserResponse;
import com.AgriTest.dto.UpdateUserRequest;
import com.AgriTest.model.User;
import com.AgriTest.service.UserService;
import com.AgriTest.util.SecurityUtils;
import com.AgriTest.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all users (Admin only)
     * @return List of all users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by ID
     * @param id User ID
     * @return User details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUserId(#id)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get current user
     * @return Current user details
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get users by role
     * @param role Role to filter by
     * @return List of users with the specified role
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String role) {
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Update user (Admin or self)
     * @param id User ID
     * @param updateRequest User update data
     * @return Updated user details
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUserId(#id)")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateRequest) {
        // Check if passwords match when provided
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            if (updateRequest.getConfirmPassword() == null || 
                !updateRequest.getPassword().equals(updateRequest.getConfirmPassword())) {
                return ResponseEntity
                    .badRequest()
                    .body(Map.of("status", 400, "message", "Error: Passwords do not match!"));
            }
        }
        
        try {
            // Convert UpdateUserRequest to User
            User userToUpdate = new User();
            userToUpdate.setUsername(updateRequest.getUsername());
            userToUpdate.setEmail(updateRequest.getEmail());
            userToUpdate.setFullName(updateRequest.getFullName());
            userToUpdate.setPhoneNumber(updateRequest.getPhoneNumber());
            userToUpdate.setRole(updateRequest.getRole());
            userToUpdate.setEnabled(updateRequest.getEnabled());
            userToUpdate.setTwoFactorEnabled(updateRequest.getTwoFactorEnabled());
            
            // Only set password if provided
            if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
                userToUpdate.setPassword(updateRequest.getPassword());
            }
            
            UserResponse updatedUser = userService.updateUser(id, userToUpdate);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", 500, "message", e.getMessage()));
        }
    }

    /**
     * Delete user by ID (Admin only)
     * @param id User ID to delete
     * @return No content response on success
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("status", 200, "message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", 500, "message", e.getMessage()));
        }
    }
    
    /**
     * Update current user
     * @param updateRequest User update data
     * @return Updated user details
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody UpdateUserRequest updateRequest) {
        Long userId = SecurityUtils.getCurrentUserId();
        return updateUser(userId, updateRequest);
    }
}