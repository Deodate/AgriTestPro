package com.AgriTest.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class RoleManagementResponse {
    private Long userId;
    private String fullName;
    private String assignedRole;
    private Set<String> permissions;
    private String status;
    private LocalDateTime lastUpdated;
    private String updatedBy;
} 