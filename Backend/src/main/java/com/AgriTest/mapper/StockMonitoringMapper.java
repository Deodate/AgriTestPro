package com.AgriTest.mapper;

import com.AgriTest.dto.StockMonitoringRequest;
import com.AgriTest.dto.StockMonitoringResponse;
import com.AgriTest.model.Product;
import com.AgriTest.model.StockMonitoring;
import org.springframework.stereotype.Component;

@Component
public class StockMonitoringMapper {
    
    public StockMonitoring toEntity(StockMonitoringRequest request, Product product) {
        StockMonitoring stockMonitoring = new StockMonitoring();
        stockMonitoring.setProduct(product);
        stockMonitoring.setCurrentStockLevel(request.getCurrentStockLevel());
        stockMonitoring.setExpiryDate(request.getExpiryDate());
        stockMonitoring.setStockAlerts(request.getStockAlerts());
        stockMonitoring.setResponsibleOfficer(request.getResponsibleOfficer());
        return stockMonitoring;
    }
    
    public StockMonitoringResponse toDto(StockMonitoring stockMonitoring) {
        StockMonitoringResponse response = new StockMonitoringResponse();
        response.setId(stockMonitoring.getId());
        response.setProductId(stockMonitoring.getProduct().getId());
        response.setProductName(stockMonitoring.getProduct().getName());
        response.setCurrentStockLevel(stockMonitoring.getCurrentStockLevel());
        response.setExpiryDate(stockMonitoring.getExpiryDate());
        response.setStockAlerts(stockMonitoring.getStockAlerts());
        response.setLastUpdatedDate(stockMonitoring.getLastUpdatedDate());
        response.setResponsibleOfficer(stockMonitoring.getResponsibleOfficer());
        return response;
    }
}