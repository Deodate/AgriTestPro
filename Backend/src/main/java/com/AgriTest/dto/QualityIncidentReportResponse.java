package com.AgriTest.dto;

import com.AgriTest.model.QualityIncidentReport.IncidentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QualityIncidentReportResponse {
    
    private Long id;
    private String incidentId;
    private Long productId;
    private String productName;
    private LocalDate incidentDate;
    private String description;
    private List<MediaFileResponse> mediaFiles;
    private String correctiveActions;
    private IncidentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}