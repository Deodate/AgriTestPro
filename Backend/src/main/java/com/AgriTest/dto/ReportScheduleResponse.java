// File: src/main/java/com/AgriTest/dto/ReportScheduleResponse.java
package com.AgriTest.dto;

import com.AgriTest.model.ExportFormat;
import com.AgriTest.model.ReportType;
import com.AgriTest.model.ScheduleFrequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportScheduleResponse {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private ReportType reportType;
    
    private ExportFormat exportFormat;
    
    private Set<Long> entityIds;
    
    private Set<String> recipients;
    
    private LocalTime scheduleTime;
    
    private ScheduleFrequency frequency;
    
    private Integer dayOfWeek;
    
    private Integer dayOfMonth;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private LocalDate nextExecution;
    
    private boolean active;
    
    private Long createdBy;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}