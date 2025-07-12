package com.AgriTest.service;

import com.AgriTest.dto.ReportGenerationRequest;
import com.AgriTest.dto.ReportGenerationResponse;
import com.AgriTest.model.ReportGeneration;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportGenerationService {
    // Create a new report generation request
    ReportGenerationResponse createReportGeneration(ReportGenerationRequest request);
    
    // Get all report generations
    List<ReportGenerationResponse> getAllReportGenerations();
    
    // Get report generation by ID
    ReportGenerationResponse getReportGenerationById(Long id);
    
    // Get reports by type
    List<ReportGenerationResponse> getReportsByType(ReportGeneration.ReportType reportType);
    
    // Get reports within a date range
    List<ReportGenerationResponse> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Update an existing report generation request
    ReportGenerationResponse updateReportGeneration(Long id, ReportGenerationRequest request);
    
    // Delete a report generation request
    void deleteReportGeneration(Long id);
}