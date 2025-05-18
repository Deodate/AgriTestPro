package com.AgriTest.repository;

import com.AgriTest.model.TimeTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTrackingRepository extends JpaRepository<TimeTracking, Long> {
    // Custom query methods can be added here if needed
} 