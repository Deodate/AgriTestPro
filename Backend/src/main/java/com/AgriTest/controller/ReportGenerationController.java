package com.AgriTest.controller;

import com.AgriTest.dto.ReportGenerationRequest;
import com.AgriTest.dto.ReportGenerationResponse;
import com.AgriTest.model.ReportGeneration;
import com.AgriTest.service.ReportGenerationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/report-generation")
public class ReportGenerationController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportGenerationController.class);
    
    @Autowired
    private ReportGenerationService reportGenerationService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORT_MANAGER')")
    public ResponseEntity<ReportGenerationResponse> createReportGeneration(
            @Valid @RequestBody ReportGenerationRequest request) {
        logger.info("Creating new report generation request for type: {}", request.getReportType());
        ReportGenerationResponse response = reportGenerationService.createReportGeneration(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORT_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<ReportGenerationResponse>> getAllReportGenerations() {
        logger.info("Fetching all report generation requests");
        List<ReportGenerationResponse> reports = reportGenerationService.getAllReportGenerations();
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORT_MANAGER') or hasRole('USER')")
    public ResponseEntity<ReportGenerationResponse> getReportGenerationById(@PathVariable Long id) {
        logger.info("Fetching report generation request with ID: {}", id);
        ReportGenerationResponse report = reportGenerationService.getReportGenerationById(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
    
    @GetMapping("/type/{reportType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORT_MANAGER')")
    public ResponseEntity<List<ReportGenerationResponse>> getReportsByType(
            @PathVariable ReportGeneration.ReportType reportType) {
        logger.info("Fetching report generation requests for type: {}", reportType);
        List<ReportGenerationResponse> reports = reportGenerationService.getReportsByType(reportType);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORT_MANAGER')")
    public ResponseEntity<List<ReportGenerationResponse>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        logger.info("Fetching report generation requests between {} and {}", startDate, endDate);
        List<ReportGenerationResponse> reports = reportGenerationService.getReportsByDateRange(startDate, endDate);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REPORT_MANAGER')")
    public ResponseEntity<ReportGenerationResponse> updateReportGeneration(
            @PathVariable Long id,
            @Valid @RequestBody ReportGenerationRequest request) {
        logger.info("Updating report generation request with ID: {}", id);
        ReportGenerationResponse response = reportGenerationService.updateReportGeneration(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReportGeneration(@PathVariable Long id) {
        logger.info("Deleting report generation request with ID: {}", id);
        reportGenerationService.deleteReportGeneration(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}