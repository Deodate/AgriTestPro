// File: src/main/java/com/AgriTest/mapper/InventoryTransactionMapper.java
package com.AgriTest.mapper;

import com.AgriTest.dto.InventoryTransactionRequest;
import com.AgriTest.dto.InventoryTransactionResponse;
import com.AgriTest.model.Inventory;
import com.AgriTest.model.InventoryTransaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryTransactionMapper {
    
    public InventoryTransactionResponse toDto(InventoryTransaction transaction) {
        if (transaction == null) {
            return null;
        }
        
        return InventoryTransactionResponse.builder()
                .id(transaction.getId())
                .inventoryId(transaction.getInventory().getId())
                .transactionType(transaction.getTransactionType())
                .quantity(transaction.getQuantity())
                .transactionDate(transaction.getTransactionDate())
                .performedBy(transaction.getPerformedBy())
                .notes(transaction.getNotes())
                .build();
    }
    
    public List<InventoryTransactionResponse> toDtoList(List<InventoryTransaction> transactions) {
        return transactions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public InventoryTransaction toEntity(InventoryTransactionRequest request, Inventory inventory, Long userId) {
        if (request == null) {
            return null;
        }
        
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setInventory(inventory);
        transaction.setTransactionType(request.getTransactionType());
        transaction.setQuantity(request.getQuantity());
        transaction.setPerformedBy(userId);
        transaction.setNotes(request.getNotes());
        transaction.setTransactionDate(LocalDateTime.now());
        
        return transaction;
    }
}