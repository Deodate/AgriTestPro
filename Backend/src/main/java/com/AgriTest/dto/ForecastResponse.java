package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponse {

    private Long modelId;
    private String modelName;
    private String dataSource;
    private List<ForecastPoint> forecastData;
    private List<ForecastPoint> historicalData; // For comparison
    private Double mape; // Mean Absolute Percentage Error
    private Double rmse; // Root Mean Square Error
    private LocalDate lastHistoricalDate;
    private String status;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastPoint {
        private LocalDate date;
        private Double value;
        private Double lowerBound;
        private Double upperBound;
        private Boolean isActual; // true for historical, false for forecast
    }
}