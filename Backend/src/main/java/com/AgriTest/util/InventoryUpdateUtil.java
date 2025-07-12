// File: src/main/java/com/AgriTest/util/InventoryUpdateUtil.java
package com.AgriTest.util;

import com.AgriTest.exception.InsufficientStockException;
import com.AgriTest.model.Product;
import com.AgriTest.model.StockMovement;
import com.AgriTest.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryUpdateUtil {
    
    @Autowired
    private ProductRepository productRepository;
    
    public void updateInventory(Product product, StockMovement.MovementType movementType, Integer quantity) {
        // Get current stock level
        Integer currentStock = product.getStockQuantity();
        
        // Update stock based on movement type
        switch (movementType) {
            case RECEIVED:
                product.setStockQuantity(currentStock + quantity);
                break;
            case ISSUED:
            case TRANSFERRED:
                if (currentStock < quantity) {
                    throw new InsufficientStockException("Insufficient stock for product: " + product.getName() + 
                            ". Available: " + currentStock + ", Requested: " + quantity);
                }
                product.setStockQuantity(currentStock - quantity);
                break;
        }
        
        // Save the updated product
        productRepository.save(product);
    }
    
    public void revertInventoryChange(Product product, StockMovement.MovementType movementType, Integer quantity) {
        // Get current stock level
        Integer currentStock = product.getStockQuantity();
        
        // Revert stock based on movement type (do the opposite of the original change)
        switch (movementType) {
            case RECEIVED:
                // If it was a receipt, subtract the quantity
                if (currentStock < quantity) {
                    throw new InsufficientStockException("Cannot revert receipt: Insufficient stock for product: " + 
                            product.getName() + ". Current stock: " + currentStock + ", Original receipt: " + quantity);
                }
                product.setStockQuantity(currentStock - quantity);
                break;
            case ISSUED:
            case TRANSFERRED:
                // If it was an issue or transfer, add the quantity back
                product.setStockQuantity(currentStock + quantity);
                break;
        }
        
        // Save the updated product
        productRepository.save(product);
    }
}