package com.AgriTest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PerformanceAnalysisRequest {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Test Case ID is required")
    @Size(min = 3, max = 50, message = "Test Case ID must be between 3 and 50 characters")
    private String testCaseId;
    
    private String trialResults;
    
    private String seasonalPerformanceMetrics;
    
    private Double effectivenessRating;
    
    private String previousSeasonComparison;
    
    private LocalDate trialDate;
}