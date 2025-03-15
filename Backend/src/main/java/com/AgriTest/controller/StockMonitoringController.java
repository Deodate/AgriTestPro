package com.AgriTest.controller;

import com.AgriTest.dto.StockMonitoringRequest;
import com.AgriTest.dto.StockMonitoringResponse;
import com.AgriTest.service.StockMonitoringService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock-monitoring")
public class StockMonitoringController {
    
    private static final Logger logger = LoggerFactory.getLogger(StockMonitoringController.class);
    
    @Autowired
    private StockMonitoringService stockMonitoringService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<StockMonitoringResponse> createStockMonitoring(
            @Valid @RequestBody StockMonitoringRequest request) {
        logger.info("Creating new stock monitoring for product ID: {}", request.getProductId());
        StockMonitoringResponse response = stockMonitoringService.createStockMonitoring(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<StockMonitoringResponse>> getAllStockMonitoring() {
        logger.info("Fetching all stock monitoring entries");
        List<StockMonitoringResponse> monitoring = stockMonitoringService.getAllStockMonitoring();
        return new ResponseEntity<>(monitoring, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<StockMonitoringResponse> getStockMonitoringById(@PathVariable Long id) {
        logger.info("Fetching stock monitoring with ID: {}", id);
        StockMonitoringResponse monitoring = stockMonitoringService.getStockMonitoringById(id);
        return new ResponseEntity<>(monitoring, HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<StockMonitoringResponse>> getStockMonitoringByProductId(
            @PathVariable Long productId) {
        logger.info("Fetching stock monitoring for product ID: {}", productId);
        List<StockMonitoringResponse> monitoring = stockMonitoringService.getStockMonitoringByProductId(productId);
        return new ResponseEntity<>(monitoring, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<StockMonitoringResponse> updateStockMonitoring(
            @PathVariable Long id, 
            @Valid @RequestBody StockMonitoringRequest request) {
        logger.info("Updating stock monitoring with ID: {}", id);
        StockMonitoringResponse response = stockMonitoringService.updateStockMonitoring(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStockMonitoring(@PathVariable Long id) {
        logger.info("Deleting stock monitoring with ID: {}", id);
        stockMonitoringService.deleteStockMonitoring(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/alerts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<List<StockMonitoringResponse>> getStockMonitoringWithAlerts() {
        logger.info("Fetching stock monitoring entries with alerts");
        List<StockMonitoringResponse> monitoring = stockMonitoringService.getStockMonitoringWithAlerts();
        return new ResponseEntity<>(monitoring, HttpStatus.OK);
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<List<StockMonitoringResponse>> getLowStockLevelMonitoring(
            @RequestParam(required = false, defaultValue = "10") Integer threshold) {
        logger.info("Fetching stock monitoring entries with stock level at or below {}", threshold);
        List<StockMonitoringResponse> monitoring = stockMonitoringService.getLowStockLevelMonitoring(threshold);
        return new ResponseEntity<>(monitoring, HttpStatus.OK);
    }
    
    @GetMapping("/upcoming-expiry")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<List<StockMonitoringResponse>> getUpcomingExpiryStockMonitoring(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate upcomingDate) {
        logger.info("Fetching stock monitoring entries expiring before {}", 
            upcomingDate != null ? upcomingDate : "default date");
        List<StockMonitoringResponse> monitoring = stockMonitoringService.getUpcomingExpiryStockMonitoring(upcomingDate);
        return new ResponseEntity<>(monitoring, HttpStatus.OK);
    }
}