package com.AgriTest.repository;

import com.AgriTest.model.ReportGeneration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportGenerationRepository extends JpaRepository<ReportGeneration, Long> {
    /**
     * Find reports that overlap with the given date range using start_date and end_date
     */
    @Query("SELECT rg FROM ReportGeneration rg " +
           "WHERE (rg.startDate BETWEEN :startDate AND :endDate) " +
           "OR (rg.endDate BETWEEN :startDate AND :endDate) " +
           "OR (rg.startDate <= :startDate AND rg.endDate >= :endDate)")
    List<ReportGeneration> findReportsByDateRange(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find reports by specific report type
     */
    List<ReportGeneration> findByReportType(ReportGeneration.ReportType reportType);

    /**
     * Find reports by product type
     */
    List<ReportGeneration> findByProductType(String productType);
}