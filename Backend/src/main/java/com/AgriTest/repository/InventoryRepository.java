package com.AgriTest.repository;

import com.AgriTest.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByProductId(Long productId);
    
    List<Inventory> findByStatus(String status);
    
    List<Inventory> findByExpirationDateBefore(LocalDate date);
    
    List<Inventory> findByLocation(String location);
    
    List<Inventory> findByBatchNumber(String batchNumber);
}