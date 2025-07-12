// File: src/main/java/com/AgriTest/mapper/StockMovementMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.StockMovementRequest;
import com.AgriTest.dto.StockMovementResponse;
import com.AgriTest.model.Product;
import com.AgriTest.model.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {
    
    public StockMovement toEntity(StockMovementRequest request, Product product) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setProduct(product);
        stockMovement.setMovementType(request.getMovementType());
        stockMovement.setQuantityMoved(request.getQuantityMoved());
        stockMovement.setReason(request.getReason());
        stockMovement.setMovementDate(request.getMovementDate());
        stockMovement.setAuthorizedBy(request.getAuthorizedBy());
        return stockMovement;
    }
    
    public StockMovementResponse toDto(StockMovement stockMovement) {
        StockMovementResponse response = new StockMovementResponse();
        response.setId(stockMovement.getId());
        response.setProductId(stockMovement.getProduct().getId());
        response.setProductName(stockMovement.getProduct().getName());
        response.setMovementType(stockMovement.getMovementType());
        response.setQuantityMoved(stockMovement.getQuantityMoved());
        response.setReason(stockMovement.getReason());
        response.setMovementDate(stockMovement.getMovementDate());
        response.setAuthorizedBy(stockMovement.getAuthorizedBy());
        response.setCreatedAt(stockMovement.getCreatedAt());
        response.setUpdatedAt(stockMovement.getUpdatedAt());
        return response;
    }
}