package com.AgriTest.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultsComparisonRequest {
    private List<Long> productIds; // Assuming comparing by product ID
    private List<Long> trialIds; // Assuming comparing by trial ID
    private String parameterToCompare;
    private String timeFrame;
    private String comparisonType; // Might not be directly used in backend data fetching but included for completeness
    private String downloadFormat;
} 