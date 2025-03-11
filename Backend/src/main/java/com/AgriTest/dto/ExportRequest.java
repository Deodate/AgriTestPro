package com.AgriTest.dto;

import com.AgriTest.model.EntityType;
import com.AgriTest.model.ExportFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportRequest {
    
    @NotBlank(message = "Data source is required")
    private String dataSource;
    
    private EntityType entityType;
    
    private Long entityId;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @NotNull(message = "Export format is required")
    private ExportFormat exportFormat;
    
    @NotEmpty(message = "At least one column must be selected")
    private List<String> columns;
    
    private String title;
}