package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalDataRequest {
    private String testName;
    private String trialPhase;
    private String dateRange; // Keep as string for now to handle frontend format
    private String productType;
    private String resultStatus;
    private String keywords;
} 