package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataResponse {
    private Long id;
    private String location;
    private LocalDate date;
    private Double temperature;
    private Double humidity;
    private Double rainfall;
    private String notes;
    private LocalDateTime recordedAt;
}