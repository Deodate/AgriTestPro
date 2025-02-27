package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransactionRequest {
    @NotNull(message = "Inventory ID is required")
    private Long inventoryId;
    
    @NotBlank(message = "Transaction type is required")
    private String transactionType;
    
    @NotNull(message = "Quantity is required")
    private Double quantity;
    
    private String notes;
}