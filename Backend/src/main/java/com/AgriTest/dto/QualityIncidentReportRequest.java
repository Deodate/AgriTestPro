package com.AgriTest.dto;

import com.AgriTest.model.QualityIncidentReport.IncidentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class QualityIncidentReportRequest {
    
    @NotBlank(message = "Incident ID is required")
    @Size(max = 50, message = "Incident ID cannot exceed 50 characters")
    private String incidentId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Incident date is required")
    private LocalDate incidentDate;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;
    
    private List<MultipartFile> mediaFiles;
    
    private String correctiveActions;
    
    @NotNull(message = "Status is required")
    private IncidentStatus status;
}