package com.AgriTest.service;

import com.AgriTest.dto.InventoryRequest;
import com.AgriTest.dto.InventoryResponse;
import com.AgriTest.dto.InventoryTransactionRequest;
import com.AgriTest.dto.InventoryTransactionResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<InventoryResponse> getAllInventoryItems();
    
    Optional<InventoryResponse> getInventoryItemById(Long id);
    
    InventoryResponse createInventoryItem(InventoryRequest inventoryRequest);
    
    InventoryResponse updateInventoryItem(Long id, InventoryRequest inventoryRequest);
    
    void deleteInventoryItem(Long id);
    
    List<InventoryResponse> getInventoryItemsByProduct(Long productId);
    
    List<InventoryResponse> getInventoryItemsByStatus(String status);
    
    List<InventoryResponse> getInventoryItemsByLocation(String location);
    
    List<InventoryResponse> getInventoryItemsByBatchNumber(String batchNumber);
    
    List<InventoryResponse> getInventoryItemsExpiringBefore(LocalDate date);
    
    InventoryTransactionResponse addInventoryTransaction(
            InventoryTransactionRequest transactionRequest, Long userId);
    
    List<InventoryTransactionResponse> getInventoryTransactionsByInventoryId(Long inventoryId);
}
