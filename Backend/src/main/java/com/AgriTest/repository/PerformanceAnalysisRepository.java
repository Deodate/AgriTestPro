package com.AgriTest.repository;

import com.AgriTest.model.PerformanceAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PerformanceAnalysisRepository extends JpaRepository<PerformanceAnalysis, Long> {
    // Find performance analyses by product ID
    List<PerformanceAnalysis> findByProductId(Long productId);
    
    // Find performance analyses by test case ID
    List<PerformanceAnalysis> findByTestCaseId(String testCaseId);
    
    // Find performance analyses within a date range
    List<PerformanceAnalysis> findByTrialDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find performance analyses with effectiveness rating above a threshold
    List<PerformanceAnalysis> findByEffectivenessRatingGreaterThan(Double threshold);
    
    // Custom query to find top N best-performing analyses
    @Query("SELECT pa FROM PerformanceAnalysis pa " +
           "ORDER BY pa.effectivenessRating DESC")
    List<PerformanceAnalysis> findTopPerformingAnalyses(int limit);
}