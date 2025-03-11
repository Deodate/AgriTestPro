package com.AgriTest.controller;

import com.AgriTest.dto.ReportScheduleRequest;
import com.AgriTest.dto.ReportScheduleResponse;
import com.AgriTest.model.ReportType;
import com.AgriTest.service.ReportScheduleService;
import com.AgriTest.util.FileNameGenerator;
import com.AgriTest.util.ReportFileNameContext;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/report-schedules")
public class ReportScheduleController {

    private final ReportScheduleService reportScheduleService;
    private final ReportFileNameContext fileNameContext;

    @Autowired
    public ReportScheduleController(
        ReportScheduleService reportScheduleService,
        ReportFileNameContext fileNameContext
    ) {
        this.reportScheduleService = reportScheduleService;
        this.fileNameContext = fileNameContext;
    }

    @PostMapping
    public ResponseEntity<ReportScheduleResponse> createReportSchedule(
        @Valid @RequestBody ReportScheduleRequest request,
        Authentication authentication
    ) {
        // Extract user ID from authentication
        Long userId = extractUserId(authentication);
        
        // Generate a preview filename for demonstration
        String previewFileName = fileNameContext.generateFileName(
            request.getReportType(), 
            request.getExportFormat(), 
            request.getEntityIds().isEmpty() 
                ? null 
                : request.getEntityIds().iterator().next()
        );
        
        // Create report schedule
        ReportScheduleResponse response = reportScheduleService.createReportSchedule(request, userId);
        
        // You could add the preview filename to the response if needed
        // response.setPreviewFileName(previewFileName);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReportScheduleResponse>> getAllReportSchedules() {
        return ResponseEntity.ok(reportScheduleService.getAllReportSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportScheduleResponse> getReportScheduleById(@PathVariable Long id) {
        return reportScheduleService.getReportScheduleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportScheduleResponse> updateReportSchedule(
        @PathVariable Long id, 
        @Valid @RequestBody ReportScheduleRequest request
    ) {
        // Generate a preview filename for demonstration
        String previewFileName = fileNameContext.generateFileName(
            request.getReportType(), 
            request.getExportFormat(), 
            request.getEntityIds().isEmpty() 
                ? null 
                : request.getEntityIds().iterator().next()
        );
        
        return ResponseEntity.ok(reportScheduleService.updateReportSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportSchedule(@PathVariable Long id) {
        reportScheduleService.deleteReportSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ReportScheduleResponse> activateReportSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(reportScheduleService.activateReportSchedule(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ReportScheduleResponse> deactivateReportSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(reportScheduleService.deactivateReportSchedule(id));
    }

    @PostMapping("/{id}/execute-now")
    public ResponseEntity<Void> executeScheduleNow(@PathVariable Long id) {
        reportScheduleService.executeScheduleNow(id);
        return ResponseEntity.ok().build();
    }

    // Helper method to extract user ID from authentication
    private Long extractUserId(Authentication authentication) {
        // Implement your user ID extraction logic
        // This might depend on your authentication mechanism
        // For example, if using JWT:
        // return Long.parseLong(authentication.getName());
        
        // Placeholder implementation
        return 1L; // Default to user ID 1 for demonstration
    }
}