// File: src/main/java/com/AgriTest/dto/StockMovementUpdateRequest.java
package com.AgriTest.dto;

import com.AgriTest.model.StockMovement.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockMovementUpdateRequest {
    
    private MovementType movementType;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantityMoved;
    
    @NotBlank(message = "Reason is required")
    private String reason;
    
    private LocalDateTime movementDate;
    
    @NotBlank(message = "Authorization information is required")
    private String authorizedBy;
}