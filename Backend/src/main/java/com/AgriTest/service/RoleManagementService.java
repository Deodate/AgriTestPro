package com.AgriTest.service;

import com.AgriTest.dto.RoleManagementRequest;
import com.AgriTest.dto.RoleManagementResponse;
import java.util.List;

public interface RoleManagementService {
    RoleManagementResponse assignRole(RoleManagementRequest request);
    RoleManagementResponse updateRole(RoleManagementRequest request);
    RoleManagementResponse getRoleManagement(Long userId);
    List<RoleManagementResponse> getAllRoleManagements();
    void deactivateRole(Long userId);
    void activateRole(Long userId);
} 