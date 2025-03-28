package com.AgriTest.controller;

import com.AgriTest.dto.RoleManagementRequest;
import com.AgriTest.dto.RoleManagementResponse;
import com.AgriTest.service.RoleManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/role-management")
@PreAuthorize("hasRole('ADMIN')")
public class RoleManagementController {
    private static final Logger logger = LoggerFactory.getLogger(RoleManagementController.class);

    @Autowired
    private RoleManagementService roleManagementService;

    @PostMapping("/assign")
    public ResponseEntity<RoleManagementResponse> assignRole(@Valid @RequestBody RoleManagementRequest request) {
        logger.info("Assigning role to user: {}", request.getUserId());
        return ResponseEntity.ok(roleManagementService.assignRole(request));
    }

    @PutMapping("/update")
    public ResponseEntity<RoleManagementResponse> updateRole(@Valid @RequestBody RoleManagementRequest request) {
        logger.info("Updating role for user: {}", request.getUserId());
        return ResponseEntity.ok(roleManagementService.updateRole(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<RoleManagementResponse> getRoleManagement(@PathVariable Long userId) {
        logger.info("Getting role management for user: {}", userId);
        return ResponseEntity.ok(roleManagementService.getRoleManagement(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleManagementResponse>> getAllRoleManagements() {
        logger.info("Getting all role managements");
        return ResponseEntity.ok(roleManagementService.getAllRoleManagements());
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateRole(@PathVariable Long userId) {
        logger.info("Deactivating role for user: {}", userId);
        roleManagementService.deactivateRole(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<Void> activateRole(@PathVariable Long userId) {
        logger.info("Activating role for user: {}", userId);
        roleManagementService.activateRole(userId);
        return ResponseEntity.ok().build();
    }
} 