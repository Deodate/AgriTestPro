package com.AgriTest.service.impl;

import com.AgriTest.dto.RoleManagementRequest;
import com.AgriTest.dto.RoleManagementResponse;
import com.AgriTest.model.User;
import com.AgriTest.repository.UserRepository;
import com.AgriTest.service.ActivityLogService;
import com.AgriTest.service.RoleManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleManagementServiceImpl implements RoleManagementService {
    private static final Logger logger = LoggerFactory.getLogger(RoleManagementServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityLogService activityLogService;

    @Override
    @Transactional
    public RoleManagementResponse assignRole(RoleManagementRequest request) {
        logger.info("Assigning role to user: {}", request.getUserId());
        
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        
        // Update user details
        user.setFullName(request.getFullName());
        user.setRole(request.getAssignedRole());
        user.setPermissions(String.join(",", request.getPermissions()));
        user.setEnabled("ACTIVE".equals(request.getStatus()));
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(getCurrentUsername());
        
        User updatedUser = userRepository.save(user);
        
        // Log the activity
        activityLogService.logActivity(
            request.getUserId(),
            String.format("Role assigned: %s with permissions: %s", 
                request.getAssignedRole(), 
                String.join(",", request.getPermissions()))
        );
        
        return mapToResponse(updatedUser);
    }

    @Override
    @Transactional
    public RoleManagementResponse updateRole(RoleManagementRequest request) {
        logger.info("Updating role for user: {}", request.getUserId());
        
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        
        // Update user details
        user.setFullName(request.getFullName());
        user.setRole(request.getAssignedRole());
        user.setPermissions(String.join(",", request.getPermissions()));
        user.setEnabled("ACTIVE".equals(request.getStatus()));
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(getCurrentUsername());
        
        User updatedUser = userRepository.save(user);
        
        // Log the activity
        activityLogService.logActivity(
            request.getUserId(),
            String.format("Role updated to: %s with permissions: %s", 
                request.getAssignedRole(), 
                String.join(",", request.getPermissions()))
        );
        
        return mapToResponse(updatedUser);
    }

    @Override
    public RoleManagementResponse getRoleManagement(Long userId) {
        logger.info("Getting role management for user: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        return mapToResponse(user);
    }

    @Override
    public List<RoleManagementResponse> getAllRoleManagements() {
        logger.info("Getting all role managements");
        
        return userRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deactivateRole(Long userId) {
        logger.info("Deactivating role for user: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setEnabled(false);
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(getCurrentUsername());
        
        userRepository.save(user);
        
        // Log the activity
        activityLogService.logActivity(userId, "User role deactivated");
    }

    @Override
    @Transactional
    public void activateRole(Long userId) {
        logger.info("Activating role for user: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setEnabled(true);
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(getCurrentUsername());
        
        userRepository.save(user);
        
        // Log the activity
        activityLogService.logActivity(userId, "User role activated");
    }

    private RoleManagementResponse mapToResponse(User user) {
        RoleManagementResponse response = new RoleManagementResponse();
        response.setUserId(user.getId());
        response.setFullName(user.getFullName());
        response.setAssignedRole(user.getRole());
        response.setPermissions(user.getPermissions() != null ? 
            Set.of(user.getPermissions().split(",")) : Set.of());
        response.setStatus(user.getEnabled() ? "ACTIVE" : "INACTIVE");
        response.setLastUpdated(user.getUpdatedAt());
        response.setUpdatedBy(user.getUpdatedBy());
        return response;
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
} 