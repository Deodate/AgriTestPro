package com.AgriTest.dto;

import lombok.Data;
import java.util.Map;

@Data
public class PerformanceAnalysisMetricsResponse {
    private Map<String, Double> metrics;
    private double performanceScore;
    private double effectivenessRating;
}