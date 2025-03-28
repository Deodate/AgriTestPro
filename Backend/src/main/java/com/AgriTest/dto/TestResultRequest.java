// File: src/main/java/com/AgriTest/dto/TestResultRequest.java
package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TestResultRequest {
    @NotNull(message = "Test case ID is required")
    private Long testCaseId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Trial phase is required")
    private String trialPhase;

    private Double growthRate;
    private Double yield;
    private Double pestResistance;

    @NotBlank(message = "Final verdict is required")
    private String finalVerdict;

    private String recommendations;

    @NotBlank(message = "Approved by is required")
    private String approvedBy;

    @NotNull(message = "Date of approval is required")
    private LocalDateTime dateOfApproval;

    @NotBlank
    private String parameterName;
    
    @NotBlank
    private String value;
    
    @NotBlank
    private String unit;
    
    private String notes;
}