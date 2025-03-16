package com.AgriTest.service;

import com.AgriTest.dto.QualityIncidentReportDTO;
import com.AgriTest.model.QualityIncidentReport;

import java.util.List;

public interface QualityIncidentReportService {
    // Create a new quality incident report
    QualityIncidentReport createQualityIncidentReport(QualityIncidentReportDTO reportDTO) throws Exception;

    // Get a quality incident report by ID
    QualityIncidentReport getQualityIncidentReportById(Long id);

    // Get a quality incident report by Incident ID
    QualityIncidentReport getQualityIncidentReportByIncidentId(String incidentId);

    // Update an existing quality incident report
    QualityIncidentReport updateQualityIncidentReport(String incidentId, QualityIncidentReportDTO reportDTO);

    // Delete a quality incident report
    void deleteQualityIncidentReport(String incidentId);

    // List all quality incident reports
    List<QualityIncidentReport> getAllQualityIncidentReports();

    // List quality incident reports by status
    List<QualityIncidentReport> getQualityIncidentReportsByStatus(QualityIncidentReport.IncidentStatus status);

    // List quality incident reports for a specific product
    List<QualityIncidentReport> getQualityIncidentReportsByProduct(Long productId);
}