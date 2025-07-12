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
public class InventoryRequest {
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Quantity is required")
    private Double quantity;
    
    @NotBlank(message = "Unit is required")
    private String unit;
    
    private String location;
    
    private LocalDate expirationDate;
    
    private String batchNumber;
    
    @NotBlank(message = "Status is required")
    private String status;
}