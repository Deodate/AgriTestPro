// File: src/main/java/com/AgriTest/dto/ReportScheduleRequest.java
package com.AgriTest.dto;

import com.AgriTest.model.ExportFormat;
import com.AgriTest.model.ReportType;
import com.AgriTest.model.ScheduleFrequency;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportScheduleRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Report type is required")
    private ReportType reportType;
    
    @NotNull(message = "Export format is required")
    private ExportFormat exportFormat;
    
    private Set<Long> entityIds;
    
    @NotEmpty(message = "At least one recipient email is required")
    private Set<@Email(message = "Invalid email format") String> recipients;
    
    @NotNull(message = "Schedule time is required")
    private LocalTime scheduleTime;
    
    @NotNull(message = "Frequency is required")
    private ScheduleFrequency frequency;
    
    private Integer dayOfWeek;
    
    private Integer dayOfMonth;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
}