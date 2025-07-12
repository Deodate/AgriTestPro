// File: src/main/java/com/AgriTest/controller/StockMovementController.java
package com.AgriTest.controller;

import com.AgriTest.dto.StockMovementRequest;
import com.AgriTest.dto.StockMovementResponse;
import com.AgriTest.dto.StockMovementUpdateRequest;
import com.AgriTest.service.StockMovementService;
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
@RequestMapping("/api/stock-movements")
public class StockMovementController {
    
    private static final Logger logger = LoggerFactory.getLogger(StockMovementController.class);
    
    @Autowired
    private StockMovementService stockMovementService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<StockMovementResponse> createStockMovement(@Valid @RequestBody StockMovementRequest request) {
        logger.info("Creating new stock movement for product ID: {}", request.getProductId());
        StockMovementResponse response = stockMovementService.createStockMovement(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<StockMovementResponse>> getAllStockMovements() {
        logger.info("Fetching all stock movements");
        List<StockMovementResponse> movements = stockMovementService.getAllStockMovements();
        return new ResponseEntity<>(movements, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<StockMovementResponse> getStockMovementById(@PathVariable Long id) {
        logger.info("Fetching stock movement with ID: {}", id);
        StockMovementResponse movement = stockMovementService.getStockMovementById(id);
        return new ResponseEntity<>(movement, HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<StockMovementResponse>> getStockMovementsByProduct(@PathVariable Long productId) {
        logger.info("Fetching stock movements for product ID: {}", productId);
        List<StockMovementResponse> movements = stockMovementService.getStockMovementsByProduct(productId);
        return new ResponseEntity<>(movements, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<StockMovementResponse> updateStockMovement(
            @PathVariable Long id, 
            @Valid @RequestBody StockMovementUpdateRequest request) {
        logger.info("Updating stock movement with ID: {}", id);
        StockMovementResponse response = stockMovementService.updateStockMovement(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
        logger.info("Deleting stock movement with ID: {}", id);
        stockMovementService.deleteStockMovement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}