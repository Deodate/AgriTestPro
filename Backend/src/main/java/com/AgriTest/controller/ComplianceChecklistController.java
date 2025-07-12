package com.AgriTest.controller;

import com.AgriTest.dto.ComplianceChecklistRequest;
import com.AgriTest.dto.ComplianceChecklistResponse;
import com.AgriTest.service.ComplianceChecklistService;
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
@RequestMapping("/api/compliance-checklist")
public class ComplianceChecklistController {
    
    private static final Logger logger = LoggerFactory.getLogger(ComplianceChecklistController.class);
    
    @Autowired
    private ComplianceChecklistService complianceChecklistService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<ComplianceChecklistResponse> createComplianceChecklist(
            @Valid @RequestBody ComplianceChecklistRequest request) {
        logger.info("Creating new compliance checklist for product name: {}", request.getProductName());
        ComplianceChecklistResponse response = complianceChecklistService.createComplianceChecklist(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<ComplianceChecklistResponse>> getAllComplianceChecklists() {
        logger.info("Fetching all compliance checklists");
        List<ComplianceChecklistResponse> checklists = complianceChecklistService.getAllComplianceChecklists();
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<ComplianceChecklistResponse> getComplianceChecklistById(@PathVariable Long id) {
        logger.info("Fetching compliance checklist with ID: {}", id);
        ComplianceChecklistResponse checklist = complianceChecklistService.getComplianceChecklistById(id);
        return new ResponseEntity<>(checklist, HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<ComplianceChecklistResponse>> getComplianceChecklistsByProductId(
            @PathVariable Long productId) {
        logger.info("Fetching compliance checklists for product ID: {}", productId);
        List<ComplianceChecklistResponse> checklists = complianceChecklistService.getComplianceChecklistsByProductId(productId);
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }
    
    @GetMapping("/reviewer/{reviewerName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<ComplianceChecklistResponse>> getComplianceChecklistsByReviewerName(
            @PathVariable String reviewerName) {
        logger.info("Fetching compliance checklists by reviewer name: {}", reviewerName);
        List<ComplianceChecklistResponse> checklists = complianceChecklistService.getComplianceChecklistsByReviewerName(reviewerName);
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<ComplianceChecklistResponse>> getComplianceChecklistsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("Fetching compliance checklists between {} and {}", startDate, endDate);
        List<ComplianceChecklistResponse> checklists = complianceChecklistService.getComplianceChecklistsByDateRange(startDate, endDate);
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<ComplianceChecklistResponse>> getMostRecentChecklists(
            @RequestParam(defaultValue = "5") int limit) {
        logger.info("Fetching {} most recent compliance checklists", limit);
        List<ComplianceChecklistResponse> checklists = complianceChecklistService.getMostRecentChecklists(limit);
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<ComplianceChecklistResponse> updateComplianceChecklist(
            @PathVariable Long id, 
            @Valid @RequestBody ComplianceChecklistRequest request) {
        logger.info("Updating compliance checklist with ID: {}", id);
        ComplianceChecklistResponse response = complianceChecklistService.updateComplianceChecklist(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComplianceChecklist(@PathVariable Long id) {
        logger.info("Deleting compliance checklist with ID: {}", id);
        complianceChecklistService.deleteComplianceChecklist(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}