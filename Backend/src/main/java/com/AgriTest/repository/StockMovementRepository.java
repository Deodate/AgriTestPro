// File: src/main/java/com/AgriTest/repository/StockMovementRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductId(Long productId);
    List<StockMovement> findByMovementType(StockMovement.MovementType movementType);
    List<StockMovement> findByMovementDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<StockMovement> findByAuthorizedBy(String authorizedBy);
}