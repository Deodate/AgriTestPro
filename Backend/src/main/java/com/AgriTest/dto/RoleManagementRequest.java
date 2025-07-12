package com.AgriTest.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Data
public class RoleManagementRequest {
    @NotNull
    private Long userId;
    
    @NotNull
    private String fullName;
    
    @NotNull
    private String assignedRole;
    
    @NotNull
    private Set<String> permissions; // READ, WRITE, APPROVE
    
    @NotNull
    private String status; // ACTIVE, INACTIVE
} 