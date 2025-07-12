package com.AgriTest.repository;

import com.AgriTest.model.RealTimeStockTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealTimeStockTrackingRepository extends JpaRepository<RealTimeStockTracking, Long> {
} 