package com.AgriTest.dto;

import com.AgriTest.model.ReportGeneration;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportGenerationResponse {
    private Long id;
    private ReportGeneration.ReportType reportType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String productType;
    private String location;
    private String status;
    private ReportGeneration.ReportFormat reportFormat;
    private Boolean emailReport;
    private LocalDateTime generatedAt;
}