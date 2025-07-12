// File: src/main/java/com/AgriTest/service/StockMovementService.java
package com.AgriTest.service;

import com.AgriTest.dto.StockMovementRequest;
import com.AgriTest.dto.StockMovementResponse;
import com.AgriTest.dto.StockMovementUpdateRequest;

import java.util.List;

public interface StockMovementService {
    StockMovementResponse createStockMovement(StockMovementRequest request);
    List<StockMovementResponse> getAllStockMovements();
    StockMovementResponse getStockMovementById(Long id);
    List<StockMovementResponse> getStockMovementsByProduct(Long productId);
    StockMovementResponse updateStockMovement(Long id, StockMovementUpdateRequest request);
    void deleteStockMovement(Long id);
}