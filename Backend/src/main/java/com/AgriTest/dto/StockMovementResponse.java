// File: src/main/java/com/AgriTest/dto/StockMovementResponse.java
package com.AgriTest.dto;

import com.AgriTest.model.StockMovement.MovementType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockMovementResponse {
    private Long id;
    private Long productId;
    private String productName;
    private MovementType movementType;
    private Integer quantityMoved;
    private String reason;
    private LocalDateTime movementDate;
    private String authorizedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}