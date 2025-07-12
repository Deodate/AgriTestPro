// File: src/main/java/com/AgriTest/repository/ReportScheduleRepository.java
package com.AgriTest.repository;

import com.AgriTest.model.ReportSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportScheduleRepository extends JpaRepository<ReportSchedule, Long> {
    
    List<ReportSchedule> findByCreatedBy(Long userId);
    
    List<ReportSchedule> findByIsActiveTrue();
    
    @Query("SELECT rs FROM ReportSchedule rs WHERE rs.isActive = true AND rs.nextExecution <= :today")
    List<ReportSchedule> findDueSchedules(LocalDate today);
    
    List<ReportSchedule> findByReportType(String reportType);
}