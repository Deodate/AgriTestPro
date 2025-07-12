package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponse {
    private Long id;
    private Long testPhaseId;
    private String parameterName;
    private String value;
    private String unit;
    private String notes;
    private Long recordedBy;
    private LocalDateTime recordedAt;
    private List<MediaFileResponse> mediaFiles;

    // Add missing fields from TestResult entity
    private Long productId;
    private String trialPhase;
    private Double growthRate;
    private Double yield;
    private Double pestResistance;
    private String finalVerdict;
    private String recommendations;
    private String approvedBy;
    private LocalDateTime dateOfApproval;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}