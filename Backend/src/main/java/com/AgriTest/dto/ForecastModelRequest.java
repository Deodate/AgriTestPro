package com.AgriTest.dto;

import com.AgriTest.model.ForecastType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastModelRequest {

    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Data source is required")
    private String dataSource;
    
    @NotNull(message = "Forecast type is required")
    private ForecastType forecastType;
    
    @NotNull(message = "Forecast horizon is required")
    @Min(value = 1, message = "Forecast horizon must be at least 1")
    private Integer forecastHorizon;
    
    // Optional model parameters
    private Map<String, Object> modelParameters;
}