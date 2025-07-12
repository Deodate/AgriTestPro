package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseStatisticsResponse {
    private Map<String, Long> statusCounts;
    private Map<String, Long> categoryCounts;
    private Double averageTestDuration;
    private Map<String, Double> successRateByCategory;
}