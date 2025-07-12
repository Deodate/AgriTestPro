package com.AgriTest.repository;

import com.AgriTest.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findByInventoryId(Long inventoryId);
    
    List<InventoryTransaction> findByTransactionType(String transactionType);
    
    List<InventoryTransaction> findByPerformedBy(Long userId);
    
    List<InventoryTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}