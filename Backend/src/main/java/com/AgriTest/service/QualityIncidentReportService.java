package com.AgriTest.service;

import com.AgriTest.dto.QualityIncidentReportRequest;
import com.AgriTest.dto.QualityIncidentReportResponse;
import com.AgriTest.model.QualityIncidentReport.IncidentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface QualityIncidentReportService {
    
    // Create a new quality incident report
    QualityIncidentReportResponse createQualityIncidentReport(QualityIncidentReportRequest request, Long userId);
    
    // Add media files to an existing report
    QualityIncidentReportResponse addMediaFiles(Long reportId, List<MultipartFile> files, Long userId);
    
    // Get all quality incident reports (paginated)
    Page<QualityIncidentReportResponse> getAllQualityIncidentReports(Pageable pageable);
    
    // Get all quality incident reports
    List<QualityIncidentReportResponse> getAllQualityIncidentReports();
    
    // Get quality incident report by ID
    QualityIncidentReportResponse getQualityIncidentReportById(Long id);
    
    // Get quality incident report by incident ID
    QualityIncidentReportResponse getQualityIncidentReportByIncidentId(String incidentId);
    
    // Get quality incident reports by product ID
    List<QualityIncidentReportResponse> getQualityIncidentReportsByProductId(Long productId);
    
    // Get quality incident reports by status
    List<QualityIncidentReportResponse> getQualityIncidentReportsByStatus(IncidentStatus status);
    
    // Get quality incident reports by date range
    List<QualityIncidentReportResponse> getQualityIncidentReportsByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Search quality incident reports by keyword
    List<QualityIncidentReportResponse> searchQualityIncidentReports(String keyword);
    
    // Update an existing quality incident report
    QualityIncidentReportResponse updateQualityIncidentReport(Long id, QualityIncidentReportRequest request);
    
    // Update the status of a quality incident report
    QualityIncidentReportResponse updateQualityIncidentReportStatus(Long id, IncidentStatus status);
    
    // Delete a quality incident report
    void deleteQualityIncidentReport(Long id);
    
    // Check if incident ID already exists
    boolean existsByIncidentId(String incidentId);
}