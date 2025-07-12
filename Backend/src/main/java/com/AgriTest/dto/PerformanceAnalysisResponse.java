package com.AgriTest.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PerformanceAnalysisResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String testCaseId;
    private String trialResults;
    private String seasonalPerformanceMetrics;
    private Double effectivenessRating;
    private String previousSeasonComparison;
    private LocalDate trialDate;
    private LocalDateTime createdAt;
}