package com.AgriTest.dto;

import com.AgriTest.model.EntityType;
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
public class TrendAnalysisRequest {

    @NotBlank(message = "Data source is required")
    private String dataSource;
    
    @NotNull(message = "Entity type is required")
    private EntityType entityType;
    
    @NotNull(message = "Entity ID is required")
    private Long entityId;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private String metricName;
    
    private String analysisType; // e.g., "LINEAR_TREND", "GROWTH_RATE", "SEASONALITY"
}