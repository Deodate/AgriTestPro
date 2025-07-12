package com.AgriTest.dto;

import com.AgriTest.model.ComplianceChecklist.ComplianceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ComplianceChecklistResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Map<String, Boolean> checklistItems;
    private String reviewerName;
    private LocalDate reviewDate;
    private String comments;
    private ComplianceStatus overallStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Calculate compliance percentage
    public Double getCompliancePercentage() {
        if (checklistItems == null || checklistItems.isEmpty()) {
            return 0.0;
        }
        
        long passCount = checklistItems.values().stream().filter(Boolean::booleanValue).count();
        return (double) passCount / checklistItems.size() * 100;
    }
}