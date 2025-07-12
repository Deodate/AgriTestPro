package com.AgriTest.dto;

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
public class WeatherDataRequest {
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    private Double temperature;
    private Double humidity;
    private Double rainfall;
    private String notes;
}