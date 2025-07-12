// File: src/main/java/com/AgriTest/mapper/InventoryMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.InventoryRequest;
import com.AgriTest.dto.InventoryResponse;
import com.AgriTest.model.Inventory;
import com.AgriTest.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryMapper {

    @Autowired
    private ProductMapper productMapper;
    
    public InventoryResponse toDto(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        
        return InventoryResponse.builder()
                .id(inventory.getId())
                .product(productMapper.toDto(inventory.getProduct()))
                .quantity(inventory.getQuantity())
                .unit(inventory.getUnit())
                .location(inventory.getLocation())
                .expirationDate(inventory.getExpirationDate())
                .batchNumber(inventory.getBatchNumber())
                .status(inventory.getStatus())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
    
    public List<InventoryResponse> toDtoList(List<Inventory> inventories) {
        return inventories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public Inventory toEntity(InventoryRequest inventoryRequest, Product product) {
        if (inventoryRequest == null) {
            return null;
        }
        
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setUnit(inventoryRequest.getUnit());
        inventory.setLocation(inventoryRequest.getLocation());
        inventory.setExpirationDate(inventoryRequest.getExpirationDate());
        inventory.setBatchNumber(inventoryRequest.getBatchNumber());
        inventory.setStatus(inventoryRequest.getStatus());
        
        return inventory;
    }
}