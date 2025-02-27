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
public class InventoryResponse {
    private Long id;
    private ProductResponse product;
    private Double quantity;
    private String unit;
    private String location;
    private LocalDate expirationDate;
    private String batchNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}