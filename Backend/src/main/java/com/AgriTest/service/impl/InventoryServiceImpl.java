package com.AgriTest.service.impl;

import com.AgriTest.dto.InventoryRequest;
import com.AgriTest.dto.InventoryResponse;
import com.AgriTest.dto.InventoryTransactionRequest;
import com.AgriTest.dto.InventoryTransactionResponse;
import com.AgriTest.dto.ProductResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.model.Inventory;
import com.AgriTest.model.InventoryTransaction;
import com.AgriTest.model.Product;
import com.AgriTest.repository.InventoryRepository;
import com.AgriTest.repository.InventoryTransactionRepository;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            InventoryTransactionRepository inventoryTransactionRepository,
            ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<InventoryResponse> getAllInventoryItems() {
        return inventoryRepository.findAll().stream()
                .map(this::mapInventoryToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventoryResponse> getInventoryItemById(Long id) {
        return inventoryRepository.findById(id)
                .map(this::mapInventoryToInventoryResponse);
    }

    @Override
    @Transactional
    public InventoryResponse createInventoryItem(InventoryRequest inventoryRequest) {
        Product product = productRepository.findById(inventoryRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryRequest.getProductId()));
        
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setUnit(inventoryRequest.getUnit());
        inventory.setLocation(inventoryRequest.getLocation());
        inventory.setExpirationDate(inventoryRequest.getExpirationDate());
        inventory.setBatchNumber(inventoryRequest.getBatchNumber());
        inventory.setStatus(inventoryRequest.getStatus());
        
        Inventory savedInventory = inventoryRepository.save(inventory);
        return mapInventoryToInventoryResponse(savedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateInventoryItem(Long id, InventoryRequest inventoryRequest) {
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
        
        Product product = productRepository.findById(inventoryRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + inventoryRequest.getProductId()));
        
        existingInventory.setProduct(product);
        existingInventory.setQuantity(inventoryRequest.getQuantity());
        existingInventory.setUnit(inventoryRequest.getUnit());
        existingInventory.setLocation(inventoryRequest.getLocation());
        existingInventory.setExpirationDate(inventoryRequest.getExpirationDate());
        existingInventory.setBatchNumber(inventoryRequest.getBatchNumber());
        existingInventory.setStatus(inventoryRequest.getStatus());
        
        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return mapInventoryToInventoryResponse(updatedInventory);
    }

    @Override
    @Transactional
    public void deleteInventoryItem(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public List<InventoryResponse> getInventoryItemsByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId).stream()
                .map(this::mapInventoryToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryItemsByStatus(String status) {
        return inventoryRepository.findByStatus(status).stream()
                .map(this::mapInventoryToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryItemsByLocation(String location) {
        return inventoryRepository.findByLocation(location).stream()
                .map(this::mapInventoryToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryItemsByBatchNumber(String batchNumber) {
        return inventoryRepository.findByBatchNumber(batchNumber).stream()
                .map(this::mapInventoryToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryResponse> getInventoryItemsExpiringBefore(LocalDate date) {
        return inventoryRepository.findByExpirationDateBefore(date).stream()
                .map(this::mapInventoryToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryTransactionResponse addInventoryTransaction(
            InventoryTransactionRequest transactionRequest, Long userId) {
        Inventory inventory = inventoryRepository.findById(transactionRequest.getInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + transactionRequest.getInventoryId()));
        
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setInventory(inventory);
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setQuantity(transactionRequest.getQuantity());
        transaction.setPerformedBy(userId);
        transaction.setNotes(transactionRequest.getNotes());
        transaction.setTransactionDate(LocalDateTime.now());
        
        // Update inventory quantity based on transaction type
        if ("STOCK_IN".equals(transactionRequest.getTransactionType())) {
            inventory.setQuantity(inventory.getQuantity() + transactionRequest.getQuantity());
        } else if ("STOCK_OUT".equals(transactionRequest.getTransactionType())) {
            if (inventory.getQuantity() < transactionRequest.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock available for this transaction");
            }
            inventory.setQuantity(inventory.getQuantity() - transactionRequest.getQuantity());
        }
        
        inventoryRepository.save(inventory);
        InventoryTransaction savedTransaction = inventoryTransactionRepository.save(transaction);
        
        return mapInventoryTransactionToResponse(savedTransaction);
    }

    @Override
    public List<InventoryTransactionResponse> getInventoryTransactionsByInventoryId(Long inventoryId) {
        return inventoryTransactionRepository.findByInventoryId(inventoryId).stream()
                .map(this::mapInventoryTransactionToResponse)
                .collect(Collectors.toList());
    }
    
    private ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .manufacturer(product.getManufacturer())
                .productType(product.getProductType())
                .activeIngredients(product.getActiveIngredients())
                .batchNumber(product.getBatchNumber())
                .imageUrl(product.getImageUrl())
                .intendedUse(product.getIntendedUse())
                .cropTarget(product.getCropTarget())
                .comments(product.getComments())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
    
    private InventoryResponse mapInventoryToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .product(mapProductToProductResponse(inventory.getProduct()))
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
    
    private InventoryTransactionResponse mapInventoryTransactionToResponse(InventoryTransaction transaction) {
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
}