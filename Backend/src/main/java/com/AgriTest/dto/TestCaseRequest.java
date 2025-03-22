package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseRequest {
    @NotBlank(message = "Test name is required")
    private String testName;
    
    private String testDescription;
    
    private String testObjectives;
    
    @NotBlank(message = "Product type is required")
    private String productType;
    
    @NotBlank(message = "Product batch number is required")
    private String productBatchNumber;
    
    @NotBlank(message = "Testing location is required")
    private String testingLocation;
    
    @NotNull(message = "Assigned worker ID is required")
    private Long assignedWorkerId;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private String notes;
}