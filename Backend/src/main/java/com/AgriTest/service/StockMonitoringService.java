package com.AgriTest.service;

import com.AgriTest.dto.StockMonitoringRequest;
import com.AgriTest.dto.StockMonitoringResponse;

import java.time.LocalDate;
import java.util.List;

public interface StockMonitoringService {
    // Create a new stock monitoring entry
    StockMonitoringResponse createStockMonitoring(StockMonitoringRequest request);
    
    // Get all stock monitoring entries
    List<StockMonitoringResponse> getAllStockMonitoring();
    
    // Get stock monitoring by ID
    StockMonitoringResponse getStockMonitoringById(Long id);
    
    // Get stock monitoring entries by product ID
    List<StockMonitoringResponse> getStockMonitoringByProductId(Long productId);
    
    // Update an existing stock monitoring entry
    StockMonitoringResponse updateStockMonitoring(Long id, StockMonitoringRequest request);
    
    // Delete a stock monitoring entry
    void deleteStockMonitoring(Long id);
    
    // Find stock monitoring entries with stock alerts
    List<StockMonitoringResponse> getStockMonitoringWithAlerts();
    
    // Find stock monitoring entries with low stock levels
    List<StockMonitoringResponse> getLowStockLevelMonitoring(Integer threshold);
    
    // Find stock monitoring entries with upcoming expiry
    List<StockMonitoringResponse> getUpcomingExpiryStockMonitoring(LocalDate upcomingDate);
}