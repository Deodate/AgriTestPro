package com.AgriTest.mapper;

import com.AgriTest.dto.ReportGenerationRequest;
import com.AgriTest.dto.ReportGenerationResponse;
import com.AgriTest.model.ReportGeneration;
import org.springframework.stereotype.Component;

@Component
public class ReportGenerationMapper {
    
    public ReportGeneration toEntity(ReportGenerationRequest request) {
        ReportGeneration reportGeneration = new ReportGeneration();
        reportGeneration.setReportType(request.getReportType());
        reportGeneration.setStartDate(request.getStartDate());
        reportGeneration.setEndDate(request.getEndDate());
        reportGeneration.setProductType(request.getProductType());
        reportGeneration.setLocation(request.getLocation());
        reportGeneration.setStatus(request.getStatus());
        reportGeneration.setReportFormat(request.getReportFormat());
        reportGeneration.setEmailReport(request.getEmailReport());
        return reportGeneration;
    }
    
    public ReportGenerationResponse toDto(ReportGeneration reportGeneration) {
        ReportGenerationResponse response = new ReportGenerationResponse();
        response.setId(reportGeneration.getId());
        response.setReportType(reportGeneration.getReportType());
        response.setStartDate(reportGeneration.getStartDate());
        response.setEndDate(reportGeneration.getEndDate());
        response.setProductType(reportGeneration.getProductType());
        response.setLocation(reportGeneration.getLocation());
        response.setStatus(reportGeneration.getStatus());
        response.setReportFormat(reportGeneration.getReportFormat());
        response.setEmailReport(reportGeneration.getEmailReport());
        response.setGeneratedAt(reportGeneration.getGeneratedAt());
        return response;
    }
}