package com.AgriTest.mapper;

import com.AgriTest.dto.PerformanceAnalysisRequest;
import com.AgriTest.dto.PerformanceAnalysisResponse;
import com.AgriTest.model.PerformanceAnalysis;
import com.AgriTest.model.Product;
import org.springframework.stereotype.Component;

@Component
public class PerformanceAnalysisMapper {
    
    public PerformanceAnalysis toEntity(PerformanceAnalysisRequest request, Product product) {
        PerformanceAnalysis performanceAnalysis = new PerformanceAnalysis();
        performanceAnalysis.setProduct(product);
        performanceAnalysis.setTestCaseId(request.getTestCaseId());
        performanceAnalysis.setTrialResults(request.getTrialResults());
        performanceAnalysis.setSeasonalPerformanceMetrics(request.getSeasonalPerformanceMetrics());
        performanceAnalysis.setEffectivenessRating(request.getEffectivenessRating());
        performanceAnalysis.setPreviousSeasonComparison(request.getPreviousSeasonComparison());
        performanceAnalysis.setTrialDate(request.getTrialDate());
        return performanceAnalysis;
    }
    
    public PerformanceAnalysisResponse toDto(PerformanceAnalysis performanceAnalysis) {
        PerformanceAnalysisResponse response = new PerformanceAnalysisResponse();
        response.setId(performanceAnalysis.getId());
        response.setProductId(performanceAnalysis.getProduct().getId());
        response.setProductName(performanceAnalysis.getProduct().getName());
        response.setTestCaseId(performanceAnalysis.getTestCaseId());
        response.setTrialResults(performanceAnalysis.getTrialResults());
        response.setSeasonalPerformanceMetrics(performanceAnalysis.getSeasonalPerformanceMetrics());
        response.setEffectivenessRating(performanceAnalysis.getEffectivenessRating());
        response.setPreviousSeasonComparison(performanceAnalysis.getPreviousSeasonComparison());
        response.setTrialDate(performanceAnalysis.getTrialDate());
        response.setCreatedAt(performanceAnalysis.getCreatedAt());
        return response;
    }
}