package com.AgriTest.controller;

import com.AgriTest.dto.PerformanceAnalysisMetricsResponse;
import com.AgriTest.dto.PerformanceAnalysisRequest;
import com.AgriTest.service.PerformanceAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/performance-analysis/metrics")
public class PerformanceAnalysisMetricsController {
    
    @Autowired
    private PerformanceAnalysisService performanceAnalysisService;
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<PerformanceAnalysisMetricsResponse> calculatePerformanceMetrics(
            @PathVariable Long id) {
        PerformanceAnalysisMetricsResponse metrics = 
            performanceAnalysisService.calculatePerformanceMetrics(id);
        return ResponseEntity.ok(metrics);
    }
    
    @PostMapping("/calculate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<PerformanceAnalysisMetricsResponse> calculatePerformanceMetricsFromRequest(
            @RequestBody PerformanceAnalysisRequest request) {
        PerformanceAnalysisMetricsResponse metrics = 
            performanceAnalysisService.calculatePerformanceMetricsFromRequest(request);
        return ResponseEntity.ok(metrics);
    }
}