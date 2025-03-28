package com.AgriTest.controller;

import com.AgriTest.dto.ReportScheduleRequest;
import com.AgriTest.dto.ReportScheduleResponse;
import com.AgriTest.service.ReportScheduleService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report-schedules")
public class ReportScheduleController {

    private final ReportScheduleService reportScheduleService;

    @Autowired
    public ReportScheduleController(
        ReportScheduleService reportScheduleService
    ) {
        this.reportScheduleService = reportScheduleService;
    }

    @PostMapping
    public ResponseEntity<ReportScheduleResponse> createReportSchedule(
        @Valid @RequestBody ReportScheduleRequest request,
        Authentication authentication
    ) {
        // Extract user ID from authentication
        Long userId = extractUserId(authentication);
        
        // Create report schedule
        ReportScheduleResponse response = reportScheduleService.createReportSchedule(request, userId);
        
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