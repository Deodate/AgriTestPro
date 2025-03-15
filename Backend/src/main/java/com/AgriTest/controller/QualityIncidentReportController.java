package com.AgriTest.controller;

import com.AgriTest.dto.QualityIncidentReportRequest;
import com.AgriTest.dto.QualityIncidentReportResponse;
import com.AgriTest.model.QualityIncidentReport.IncidentStatus;
import com.AgriTest.service.QualityIncidentReportService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/quality-incident")
public class QualityIncidentReportController {
    
    private static final Logger logger = LoggerFactory.getLogger(QualityIncidentReportController.class);
    
    @Autowired
    private QualityIncidentReportService qualityIncidentReportService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<QualityIncidentReportResponse> createQualityIncidentReport(
            @Valid @ModelAttribute QualityIncidentReportRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        logger.info("Creating new quality incident report with ID: {}", request.getIncidentId());
        
        // Assuming you have a way to get the user ID from UserDetails
        Long userId = getUserIdFromUserDetails(userDetails);
        
        QualityIncidentReportResponse response = qualityIncidentReportService.createQualityIncidentReport(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/{id}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<QualityIncidentReportResponse> addMediaFiles(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        logger.info("Adding media files to quality incident report with ID: {}", id);
        
        // Assuming you have a way to get the user ID from UserDetails
        Long userId = getUserIdFromUserDetails(userDetails);
        
        QualityIncidentReportResponse response = qualityIncidentReportService.addMediaFiles(id, files, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<QualityIncidentReportResponse>> getAllQualityIncidentReports() {
        logger.info("Fetching all quality incident reports");
        List<QualityIncidentReportResponse> reports = qualityIncidentReportService.getAllQualityIncidentReports();
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/paged")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<Page<QualityIncidentReportResponse>> getPagedQualityIncidentReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "incidentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("Fetching paged quality incident reports - page: {}, size: {}", page, size);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<QualityIncidentReportResponse> reports = qualityIncidentReportService.getAllQualityIncidentReports(pageRequest);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<QualityIncidentReportResponse> getQualityIncidentReportById(@PathVariable Long id) {
        logger.info("Fetching quality incident report with ID: {}", id);
        QualityIncidentReportResponse report = qualityIncidentReportService.getQualityIncidentReportById(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
    
    @GetMapping("/incident/{incidentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<QualityIncidentReportResponse> getQualityIncidentReportByIncidentId(@PathVariable String incidentId) {
        logger.info("Fetching quality incident report with incident ID: {}", incidentId);
        QualityIncidentReportResponse report = qualityIncidentReportService.getQualityIncidentReportByIncidentId(incidentId);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<QualityIncidentReportResponse>> getQualityIncidentReportsByProductId(@PathVariable Long productId) {
        logger.info("Fetching quality incident reports for product ID: {}", productId);
        List<QualityIncidentReportResponse> reports = qualityIncidentReportService.getQualityIncidentReportsByProductId(productId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<QualityIncidentReportResponse>> getQualityIncidentReportsByStatus(@PathVariable IncidentStatus status) {
        logger.info("Fetching quality incident reports with status: {}", status);
        List<QualityIncidentReportResponse> reports = qualityIncidentReportService.getQualityIncidentReportsByStatus(status);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<QualityIncidentReportResponse>> getQualityIncidentReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        logger.info("Fetching quality incident reports between {} and {}", startDate, endDate);
        List<QualityIncidentReportResponse> reports = qualityIncidentReportService.getQualityIncidentReportsByDateRange(startDate, endDate);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<List<QualityIncidentReportResponse>> searchQualityIncidentReports(@RequestParam String keyword) {
        logger.info("Searching quality incident reports with keyword: {}", keyword);
        List<QualityIncidentReportResponse> reports = qualityIncidentReportService.searchQualityIncidentReports(keyword);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<QualityIncidentReportResponse> updateQualityIncidentReport(
            @PathVariable Long id, 
            @Valid @RequestBody QualityIncidentReportRequest request) {
        
        logger.info("Updating quality incident report with ID: {}", id);
        QualityIncidentReportResponse response = qualityIncidentReportService.updateQualityIncidentReport(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('QUALITY_MANAGER') or hasRole('RESEARCH_MANAGER')")
    public ResponseEntity<QualityIncidentReportResponse> updateQualityIncidentReportStatus(
            @PathVariable Long id,
            @RequestParam IncidentStatus status) {
        
        logger.info("Updating status of quality incident report with ID: {} to {}", id, status);
        QualityIncidentReportResponse response = qualityIncidentReportService.updateQualityIncidentReportStatus(id, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQualityIncidentReport(@PathVariable Long id) {
        logger.info("Deleting quality incident report with ID: {}", id);
        qualityIncidentReportService.deleteQualityIncidentReport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    // Helper method to get user ID from UserDetails
    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        // This implementation depends on your security setup
        // Here's a simple example assuming you have a custom UserDetails implementation
        if (userDetails instanceof CustomUserDetails) {
            return ((CustomUserDetails) userDetails).getId();
        }
        // Default to admin user or handle as appropriate for your application
        return 1L; // For testing purposes
    }
    
    // Inner class for custom UserDetails (replace with your actual implementation)
    private static class CustomUserDetails implements UserDetails {
        private final Long id;
        private final String username;
        
        public CustomUserDetails(Long id, String username) {
            this.id = id;
            this.username = username;
        }
        
        public Long getId() {
            return id;
        }
        
        @Override
        public String getUsername() {
            return username;
        }
        
        // Implement other required methods
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
        
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
        
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return java.util.Collections.emptyList();
        }
        
        @Override
        public String getPassword() {
            return null;
        }
    }
}