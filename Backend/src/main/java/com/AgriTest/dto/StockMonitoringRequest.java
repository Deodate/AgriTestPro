package com.AgriTest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StockMonitoringRequest {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Current stock level is required")
    @Min(value = 0, message = "Current stock level must be non-negative")
    private Integer currentStockLevel;
    
    private LocalDate expiryDate;
    
    @NotNull(message = "Stock alerts setting is required")
    private Boolean stockAlerts;
    
    @NotBlank(message = "Responsible officer is required")
    @Size(min = 2, max = 100, message = "Responsible officer name must be between 2 and 100 characters")
    private String responsibleOfficer;
}