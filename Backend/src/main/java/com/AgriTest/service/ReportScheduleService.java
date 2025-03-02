// File: src/main/java/com/AgriTest/service/ReportScheduleService.java
package com.AgriTest.service;

import com.AgriTest.dto.ReportScheduleRequest;
import com.AgriTest.dto.ReportScheduleResponse;
import com.AgriTest.model.ReportType;

import java.util.List;
import java.util.Optional;

public interface ReportScheduleService {
    
    /**
     * Get all report schedules
     */
    List<ReportScheduleResponse> getAllReportSchedules();
    
    /**
     * Get a report schedule by ID
     */
    Optional<ReportScheduleResponse> getReportScheduleById(Long id);
    
    /**
     * Create a new report schedule
     */
    ReportScheduleResponse createReportSchedule(ReportScheduleRequest request, Long userId);
    
    /**
     * Update an existing report schedule
     */
    ReportScheduleResponse updateReportSchedule(Long id, ReportScheduleRequest request);
    
    /**
     * Delete a report schedule
     */
    void deleteReportSchedule(Long id);
    
    /**
     * Get report schedules created by a specific user
     */
    List<ReportScheduleResponse> getReportSchedulesByUser(Long userId);
    
    /**
     * Get report schedules by report type
     */
    List<ReportScheduleResponse> getReportSchedulesByType(ReportType reportType);
    
    /**
     * Activate a report schedule
     */
    ReportScheduleResponse activateReportSchedule(Long id);
    
    /**
     * Deactivate a report schedule
     */
    ReportScheduleResponse deactivateReportSchedule(Long id);
    
    /**
     * Process all due report schedules
     */
    void processDueSchedules();
    
    /**
     * Manually run a report schedule immediately
     */
    void executeScheduleNow(Long id);
}