package com.AgriTest.repository;

import com.AgriTest.model.StockMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockMonitoringRepository extends JpaRepository<StockMonitoring, Long> {
    @Query("SELECT sm FROM StockMonitoring sm " +
           "JOIN FETCH sm.product p " +
           "WHERE sm.currentStockLevel <= :threshold " +
           "ORDER BY sm.currentStockLevel ASC")
    List<StockMonitoring> findLowStockEntriesWithProductDetails(
        Integer threshold
    );

    // Additional methods remain the same
    List<StockMonitoring> findByProductId(Long productId);
    List<StockMonitoring> findByStockAlertsTrue();
    List<StockMonitoring> findByResponsibleOfficer(String responsibleOfficer);
    List<StockMonitoring> findByExpiryDateBefore(LocalDate date);
}