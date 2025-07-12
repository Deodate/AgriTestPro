package com.AgriTest.dto;

import com.AgriTest.model.ForecastType;
import com.AgriTest.model.ModelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastModelResponse {

    private Long id;
    private String name;
    private String description;
    private String dataSource;
    private ForecastType forecastType;
    private Integer forecastHorizon;
    private Boolean isActive;
    private ModelStatus status;
    private LocalDateTime lastTrainedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Map<String, Object> modelParameters;
}