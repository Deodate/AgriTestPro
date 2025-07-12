package com.AgriTest.controller;

import com.AgriTest.dto.RealTimeStockTrackingRequest;
import com.AgriTest.dto.RealTimeStockTrackingResponse;
import com.AgriTest.service.RealTimeStockTrackingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/real-time-stock-tracking")
public class RealTimeStockTrackingController {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeStockTrackingController.class);

    @Autowired
    private RealTimeStockTrackingService realTimeStockTrackingService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<RealTimeStockTrackingResponse> createRealTimeStockTracking(
            @Valid @RequestBody RealTimeStockTrackingRequest request) {
        logger.info("Creating new real-time stock tracking entry for product: {}", request.getProductName());
        RealTimeStockTrackingResponse response = realTimeStockTrackingService.createRealTimeStockTracking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<RealTimeStockTrackingResponse>> getAllRealTimeStockTracking() {
        logger.info("Fetching all real-time stock tracking entries");
        List<RealTimeStockTrackingResponse> entries = realTimeStockTrackingService.getAllRealTimeStockTracking();
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<RealTimeStockTrackingResponse> getRealTimeStockTrackingById(@PathVariable Long id) {
        logger.info("Fetching real-time stock tracking entry with ID: {}", id);
        RealTimeStockTrackingResponse entry = realTimeStockTrackingService.getRealTimeStockTrackingById(id);
        return new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<RealTimeStockTrackingResponse> updateRealTimeStockTracking(
            @PathVariable Long id,
            @Valid @RequestBody RealTimeStockTrackingRequest request) {
        logger.info("Updating real-time stock tracking entry with ID: {}", id);
        RealTimeStockTrackingResponse response = realTimeStockTrackingService.updateRealTimeStockTracking(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRealTimeStockTracking(@PathVariable Long id) {
        logger.info("Deleting real-time stock tracking entry with ID: {}", id);
        realTimeStockTrackingService.deleteRealTimeStockTracking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 