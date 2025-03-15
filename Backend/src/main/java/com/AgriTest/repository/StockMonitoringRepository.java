package com.AgriTest.repository;

import com.AgriTest.model.StockMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockMonitoringRepository extends JpaRepository<StockMonitoring, Long> {
    // Find by product ID
    List<StockMonitoring> findByProductId(Long productId);
    
    // Find stock monitoring with stock alerts enabled
    List<StockMonitoring> findByStockAlertsTrue();
    
    // Find stock monitoring by responsible officer
    List<StockMonitoring> findByResponsibleOfficer(String responsibleOfficer);
    
    // Find stock monitoring by expiry date before a specific date
    List<StockMonitoring> findByExpiryDateBefore(LocalDate date);
    
    // Find stock monitoring with low stock levels
    List<StockMonitoring> findByCurrentStockLevelLessThan(Integer threshold);
}