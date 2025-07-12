package com.AgriTest.dto;

import com.AgriTest.model.StockMovement.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockMovementRequest {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Movement type is required")
    private MovementType movementType;
    
    @NotNull(message = "Quantity moved is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantityMoved;
    
    @NotBlank(message = "Reason is required")
    private String reason;
    
    @NotNull(message = "Movement date is required")
    private LocalDateTime movementDate;
    
    @NotBlank(message = "Authorization information is required")
    private String authorizedBy;
}