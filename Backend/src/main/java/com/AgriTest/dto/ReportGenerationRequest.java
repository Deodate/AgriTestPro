package com.AgriTest.dto;

import com.AgriTest.model.ReportGeneration;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportGenerationRequest {
    
    @NotNull(message = "Report type is required")
    private ReportGeneration.ReportType reportType;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
    private String productType;
    private String location;
    private String status;
    
    @NotNull(message = "Report format is required")
    private ReportGeneration.ReportFormat reportFormat;
    
    @NotNull(message = "Email report flag is required")
    private Boolean emailReport;
}