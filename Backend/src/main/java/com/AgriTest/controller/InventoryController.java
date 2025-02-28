// File: src/main/java/com/AgriTest/controller/InventoryController.java
package com.AgriTest.controller;

import com.AgriTest.dto.InventoryRequest;
import com.AgriTest.dto.InventoryResponse;
import com.AgriTest.dto.InventoryTransactionRequest;
import com.AgriTest.dto.InventoryTransactionResponse;
import com.AgriTest.service.InventoryService;
import com.AgriTest.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<InventoryResponse> getAllInventoryItems() {
        return inventoryService.getAllInventoryItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryItemById(@PathVariable Long id) {
        return inventoryService.getInventoryItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public InventoryResponse createInventoryItem(@Valid @RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.createInventoryItem(inventoryRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryResponse> updateInventoryItem(@PathVariable Long id, @Valid @RequestBody InventoryRequest inventoryRequest) {
        return ResponseEntity.ok(inventoryService.updateInventoryItem(id, inventoryRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        inventoryService.deleteInventoryItem(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{productId}")
    public List<InventoryResponse> getInventoryItemsByProduct(@PathVariable Long productId) {
        return inventoryService.getInventoryItemsByProduct(productId);
    }

    @GetMapping("/status/{status}")
    public List<InventoryResponse> getInventoryItemsByStatus(@PathVariable String status) {
        return inventoryService.getInventoryItemsByStatus(status);
    }

    @GetMapping("/location/{location}")
    public List<InventoryResponse> getInventoryItemsByLocation(@PathVariable String location) {
        return inventoryService.getInventoryItemsByLocation(location);
    }

    @GetMapping("/batch/{batchNumber}")
    public List<InventoryResponse> getInventoryItemsByBatchNumber(@PathVariable String batchNumber) {
        return inventoryService.getInventoryItemsByBatchNumber(batchNumber);
    }

    @GetMapping("/expiring-before")
    public List<InventoryResponse> getInventoryItemsExpiringBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return inventoryService.getInventoryItemsExpiringBefore(date);
    }

    @PostMapping("/{id}/transactions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public InventoryTransactionResponse addInventoryTransaction(
            @PathVariable Long id, @Valid @RequestBody InventoryTransactionRequest transactionRequest) {
        Long userId = SecurityUtils.getCurrentUserId();
        return inventoryService.addInventoryTransaction(transactionRequest, userId);
    }

    @GetMapping("/{id}/transactions")
    public List<InventoryTransactionResponse> getInventoryTransactions(@PathVariable Long id) {
        return inventoryService.getInventoryTransactionsByInventoryId(id);
    }
}