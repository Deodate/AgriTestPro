// File: src/main/java/com/AgriTest/service/impl/StockMovementServiceImpl.java
package com.AgriTest.service.impl;

import com.AgriTest.dto.StockMovementRequest;
import com.AgriTest.dto.StockMovementResponse;
import com.AgriTest.dto.StockMovementUpdateRequest;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.StockMovementMapper;
import com.AgriTest.model.Product;
import com.AgriTest.model.StockMovement;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.repository.StockMovementRepository;
import com.AgriTest.service.StockMovementService;
import com.AgriTest.util.InventoryUpdateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMovementServiceImpl implements StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockMovementMapper stockMovementMapper;
    
    @Autowired
    private InventoryUpdateUtil inventoryUpdateUtil;
    
    @Override
    @Transactional
    public StockMovementResponse createStockMovement(StockMovementRequest request) {
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Create stock movement entity
        StockMovement stockMovement = stockMovementMapper.toEntity(request, product);
        
        // Update inventory based on movement type
        inventoryUpdateUtil.updateInventory(product, request.getMovementType(), request.getQuantityMoved());
        
        // Save the stock movement
        StockMovement savedMovement = stockMovementRepository.save(stockMovement);
        
        // Return the DTO
        return stockMovementMapper.toDto(savedMovement);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getAllStockMovements() {
        return stockMovementRepository.findAll().stream()
                .map(stockMovementMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public StockMovementResponse getStockMovementById(Long id) {
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        
        return stockMovementMapper.toDto(stockMovement);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getStockMovementsByProduct(Long productId) {
        // Verify the product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        return stockMovementRepository.findByProductId(productId).stream()
                .map(stockMovementMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public StockMovementResponse updateStockMovement(Long id, StockMovementUpdateRequest request) {
        // Find the existing stock movement
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        
        // Get the product
        Product product = stockMovement.getProduct();
        
        // Revert the previous inventory change
        inventoryUpdateUtil.revertInventoryChange(product, stockMovement.getMovementType(), stockMovement.getQuantityMoved());
        
        // Update the stock movement with new values
        stockMovement.setReason(request.getReason());
        stockMovement.setMovementDate(request.getMovementDate());
        stockMovement.setAuthorizedBy(request.getAuthorizedBy());
        
        // If movement type or quantity changed, update them and recalculate inventory
        if (request.getMovementType() != null) {
            stockMovement.setMovementType(request.getMovementType());
        }
        
        if (request.getQuantityMoved() != null) {
            stockMovement.setQuantityMoved(request.getQuantityMoved());
        }
        
        // Apply the new inventory change
        inventoryUpdateUtil.updateInventory(product, stockMovement.getMovementType(), stockMovement.getQuantityMoved());
        
        // Save the updated stock movement
        StockMovement updatedMovement = stockMovementRepository.save(stockMovement);
        
        return stockMovementMapper.toDto(updatedMovement);
    }
    
    @Override
    @Transactional
    public void deleteStockMovement(Long id) {
        // Find the stock movement
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock movement not found with id: " + id));
        
        // Get the product
        Product product = stockMovement.getProduct();
        
        // Revert the inventory change
        inventoryUpdateUtil.revertInventoryChange(product, stockMovement.getMovementType(), stockMovement.getQuantityMoved());
        
        // Delete the stock movement
        stockMovementRepository.delete(stockMovement);
    }
}