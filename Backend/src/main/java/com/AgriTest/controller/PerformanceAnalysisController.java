package com.AgriTest.controller;

import com.AgriTest.dto.PerformanceAnalysisRequest;
import com.AgriTest.dto.PerformanceAnalysisResponse;
import com.AgriTest.service.PerformanceAnalysisService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/performance-analysis")
public class PerformanceAnalysisController {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceAnalysisController.class);
    
    @Autowired
    private PerformanceAnalysisService performanceAnalysisService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<PerformanceAnalysisResponse> createPerformanceAnalysis(
            @Valid @RequestBody PerformanceAnalysisRequest request) {
        logger.info("Creating new performance analysis for product ID: {}", request.getProductId());
        PerformanceAnalysisResponse response = performanceAnalysisService.createPerformanceAnalysis(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<PerformanceAnalysisResponse>> getAllPerformanceAnalyses() {
        logger.info("Fetching all performance analyses");
        List<PerformanceAnalysisResponse> analyses = performanceAnalysisService.getAllPerformanceAnalyses();
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<PerformanceAnalysisResponse> getPerformanceAnalysisById(@PathVariable Long id) {
        logger.info("Fetching performance analysis with ID: {}", id);
        PerformanceAnalysisResponse analysis = performanceAnalysisService.getPerformanceAnalysisById(id);
        return new ResponseEntity<>(analysis, HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<PerformanceAnalysisResponse>> getPerformanceAnalysesByProductId(
            @PathVariable Long productId) {
        logger.info("Fetching performance analyses for product ID: {}", productId);
        List<PerformanceAnalysisResponse> analyses = performanceAnalysisService.getPerformanceAnalysesByProductId(productId);
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }
    
    @GetMapping("/test-case/{testCaseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<PerformanceAnalysisResponse>> getPerformanceAnalysesByTestCaseId(
            @PathVariable String testCaseId) {
        logger.info("Fetching performance analyses for test case ID: {}", testCaseId);
        List<PerformanceAnalysisResponse> analyses = performanceAnalysisService.getPerformanceAnalysesByTestCaseId(testCaseId);
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<PerformanceAnalysisResponse>> getPerformanceAnalysesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("Fetching performance analyses between {} and {}", startDate, endDate);
        List<PerformanceAnalysisResponse> analyses = performanceAnalysisService.getPerformanceAnalysesByDateRange(startDate, endDate);
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }
    
    @GetMapping("/top-performers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<PerformanceAnalysisResponse>> getTopPerformingAnalyses(
            @RequestParam(defaultValue = "5") int limit) {
        logger.info("Fetching top {} performing analyses", limit);
        List<PerformanceAnalysisResponse> analyses = performanceAnalysisService.getTopPerformingAnalyses(limit);
        return new ResponseEntity<>(analyses, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<PerformanceAnalysisResponse> updatePerformanceAnalysis(
            @PathVariable Long id, 
            @Valid @RequestBody PerformanceAnalysisRequest request) {
        logger.info("Updating performance analysis with ID: {}", id);
        PerformanceAnalysisResponse response = performanceAnalysisService.updatePerformanceAnalysis(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePerformanceAnalysis(@PathVariable Long id) {
        logger.info("Deleting performance analysis with ID: {}", id);
        performanceAnalysisService.deletePerformanceAnalysis(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}