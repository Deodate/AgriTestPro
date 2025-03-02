// File: src/main/java/com/AgriTest/controller/ExportController.java
package com.AgriTest.controller;

import com.AgriTest.model.ExportFormat;
import com.AgriTest.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportService exportService;
    
    /**
     * Export test cases to various formats
     * @param format The export format (CSV, EXCEL, PDF)
     * @param testCaseIds Optional list of test case IDs to export. If empty, exports all.
     */
    @GetMapping("/testcases")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<byte[]> exportTestCases(
            @RequestParam ExportFormat format,
            @RequestParam(required = false) List<Long> testCaseIds) {
        
        byte[] fileContent = exportService.exportTestCases(format, testCaseIds);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "test_cases_" + timestamp + getFileExtension(format);
        
        return ResponseEntity.ok()
                .contentType(getMediaType(format))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(fileContent);
    }
    
    /**
     * Export test schedules to various formats
     * @param format The export format (CSV, EXCEL, PDF)
     * @param scheduleIds Optional list of schedule IDs to export. If empty, exports all.
     */
    @GetMapping("/schedules")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<byte[]> exportSchedules(
            @RequestParam ExportFormat format,
            @RequestParam(required = false) List<Long> scheduleIds) {
        
        byte[] fileContent = exportService.exportTestSchedules(format, scheduleIds);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "schedules_" + timestamp + getFileExtension(format);
        
        return ResponseEntity.ok()
                .contentType(getMediaType(format))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(fileContent);
    }
    
    /**
     * Export test results to various formats
     * @param format The export format (CSV, EXCEL, PDF)
     * @param testCaseId The ID of the test case to export results for
     */
    @GetMapping("/results/{testCaseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<byte[]> exportTestResults(
            @RequestParam ExportFormat format,
            @PathVariable Long testCaseId) {
        
        byte[] fileContent = exportService.exportTestResults(format, testCaseId);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "test_results_" + timestamp + getFileExtension(format);
        
        return ResponseEntity.ok()
                .contentType(getMediaType(format))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(fileContent);
    }
    
    /**
     * Helper method to get the file extension based on format
     */
    private String getFileExtension(ExportFormat format) {
        switch (format) {
            case CSV:
                return ".csv";
            case EXCEL:
                return ".xlsx";
            case PDF:
                return ".pdf";
            default:
                return ".txt";
        }
    }
    
    /**
     * Helper method to get the media type based on format
     */
    private MediaType getMediaType(ExportFormat format) {
        switch (format) {
            case CSV:
                return MediaType.parseMediaType("text/csv");
            case EXCEL:
                return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case PDF:
                return MediaType.parseMediaType("application/pdf");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}