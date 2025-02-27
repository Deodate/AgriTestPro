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
public class InventoryStatisticsResponse {
    private Map<String, Long> productCounts;
    private Map<String, Long> locationCounts;
    private Double totalInventoryValue;
    private Map<String, Double> valueByCategory;
}