package com.AgriTest.service;

import com.AgriTest.dto.PerformanceAnalysisMetricsResponse;
import com.AgriTest.dto.PerformanceAnalysisRequest;
import com.AgriTest.dto.PerformanceAnalysisResponse;

import java.time.LocalDate;
import java.util.List;

public interface PerformanceAnalysisService {
    // Create a new performance analysis
    PerformanceAnalysisResponse createPerformanceAnalysis(PerformanceAnalysisRequest request);
    
    // Get all performance analyses
    List<PerformanceAnalysisResponse> getAllPerformanceAnalyses();
    
    // Get performance analysis by ID
    PerformanceAnalysisResponse getPerformanceAnalysisById(Long id);
    
    // Get performance analyses by product ID
    List<PerformanceAnalysisResponse> getPerformanceAnalysesByProductId(Long productId);
    
    // Get performance analyses by test case ID
    List<PerformanceAnalysisResponse> getPerformanceAnalysesByTestCaseId(String testCaseId);
    
    // Get performance analyses by date range
    List<PerformanceAnalysisResponse> getPerformanceAnalysesByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Get top performing analyses
    List<PerformanceAnalysisResponse> getTopPerformingAnalyses(int limit);
    
    // Update an existing performance analysis
    PerformanceAnalysisResponse updatePerformanceAnalysis(Long id, PerformanceAnalysisRequest request);
    
    // Delete a performance analysis
    void deletePerformanceAnalysis(Long id);
    
    /**
     * Calculate performance metrics for a specific performance analysis
     * @param id Performance analysis ID
     * @return Performance metrics response
     */
    PerformanceAnalysisMetricsResponse calculatePerformanceMetrics(Long id);
    
    /**
     * Calculate performance metrics for a performance analysis request
     * @param request Performance analysis request
     * @return Performance metrics response
     */
    PerformanceAnalysisMetricsResponse calculatePerformanceMetricsFromRequest(
        PerformanceAnalysisRequest request
    );
}