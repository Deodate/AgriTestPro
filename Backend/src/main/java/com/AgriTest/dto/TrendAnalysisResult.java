package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendAnalysisResult {

    private String dataSource;
    private String entityName;
    private LocalDate startDate;
    private LocalDate endDate;
    
    // Time series data points
    private List<DataPoint> timeSeriesData;
    
    // Trend metrics
    private Double growthRate;
    private Double averageValue;
    private Double minValue;
    private Double maxValue;
    private Double standardDeviation;
    private Double slopeCoefficient;
    private Double rSquared;
    private Boolean hasSeasonality;
    private Integer seasonalPeriod; // If seasonality is detected
    
    // Additional analysis data
    private Map<String, Object> additionalMetrics;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPoint {
        private LocalDate date;
        private Double value;
        private Double trendValue; // Trend component (if decomposed)
    }
}