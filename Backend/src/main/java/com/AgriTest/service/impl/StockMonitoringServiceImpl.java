package com.AgriTest.service.impl;

import com.AgriTest.dto.StockMonitoringRequest;
import com.AgriTest.dto.StockMonitoringResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.StockMonitoringMapper;
import com.AgriTest.model.Product;
import com.AgriTest.model.StockMonitoring;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.repository.StockMonitoringRepository;
import com.AgriTest.service.StockMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMonitoringServiceImpl implements StockMonitoringService {
    
    @Autowired
    private StockMonitoringRepository stockMonitoringRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockMonitoringMapper stockMonitoringMapper;
    
    @Override
    @Transactional
    public StockMonitoringResponse createStockMonitoring(StockMonitoringRequest request) {
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Create stock monitoring entity
        StockMonitoring stockMonitoring = stockMonitoringMapper.toEntity(request, product);
        
        // Save the stock monitoring
        StockMonitoring savedMonitoring = stockMonitoringRepository.save(stockMonitoring);
        
        // Return the DTO
        return stockMonitoringMapper.toDto(savedMonitoring);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StockMonitoringResponse> getAllStockMonitoring() {
        return stockMonitoringRepository.findAll().stream()
                .map(stockMonitoringMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public StockMonitoringResponse getStockMonitoringById(Long id) {
        StockMonitoring stockMonitoring = stockMonitoringRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock monitoring not found with id: " + id));
        
        return stockMonitoringMapper.toDto(stockMonitoring);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StockMonitoringResponse> getStockMonitoringByProductId(Long productId) {
        // Verify the product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        return stockMonitoringRepository.findByProductId(productId).stream()
                .map(stockMonitoringMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public StockMonitoringResponse updateStockMonitoring(Long id, StockMonitoringRequest request) {
        // Find the existing stock monitoring entry
        StockMonitoring stockMonitoring = stockMonitoringRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock monitoring not found with id: " + id));
        
        // Get the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Update the stock monitoring with new values
        stockMonitoring.setProduct(product);
        stockMonitoring.setCurrentStockLevel(request.getCurrentStockLevel());
        stockMonitoring.setExpiryDate(request.getExpiryDate());
        stockMonitoring.setStockAlerts(request.getStockAlerts());
        stockMonitoring.setResponsibleOfficer(request.getResponsibleOfficer());
        
        // Save the updated stock monitoring
        StockMonitoring updatedMonitoring = stockMonitoringRepository.save(stockMonitoring);
        
        return stockMonitoringMapper.toDto(updatedMonitoring);
    }
    
    @Override
    @Transactional
    public void deleteStockMonitoring(Long id) {
        // Find the stock monitoring entry
        StockMonitoring stockMonitoring = stockMonitoringRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock monitoring not found with id: " + id));
        
        // Delete the stock monitoring
        stockMonitoringRepository.delete(stockMonitoring);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StockMonitoringResponse> getStockMonitoringWithAlerts() {
        return stockMonitoringRepository.findByStockAlertsTrue().stream()
                .map(stockMonitoringMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StockMonitoringResponse> getLowStockLevelMonitoring(Integer threshold) {
        return stockMonitoringRepository.findByCurrentStockLevelLessThan(threshold).stream()
                .map(stockMonitoringMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StockMonitoringResponse> getUpcomingExpiryStockMonitoring(LocalDate upcomingDate) {
        return stockMonitoringRepository.findByExpiryDateBefore(upcomingDate).stream()
                .map(stockMonitoringMapper::toDto)
                .collect(Collectors.toList());
    }
}