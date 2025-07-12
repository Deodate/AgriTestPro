package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ComplianceChecklistRequest {
    
    @NotBlank(message = "Product name is required")
    private String productName;
    
    @NotEmpty(message = "Checklist items are required")
    private Map<String, Boolean> checklistItems;
    
    @NotBlank(message = "Reviewer name is required")
    private String reviewerName;
    
    @NotNull(message = "Review date is required")
    private LocalDate reviewDate;
    
    private String comments;
}