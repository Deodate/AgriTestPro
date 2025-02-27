package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransactionResponse {
    private Long id;
    private Long inventoryId;
    private String transactionType;
    private Double quantity;
    private LocalDateTime transactionDate;
    private Long performedBy;
    private String notes;
}