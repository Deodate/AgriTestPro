package com.AgriTest.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StockMonitoringResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer currentStockLevel;
    private LocalDate expiryDate;
    private Boolean stockAlerts;
    private LocalDate lastUpdatedDate;
    private String responsibleOfficer;
}