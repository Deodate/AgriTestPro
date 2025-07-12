package com.AgriTest.controller;

import com.AgriTest.dto.ExportRequest;
import com.AgriTest.model.ExportFormat;
import com.AgriTest.service.DataExportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/analytics/export")
public class DataExportController {

    @Autowired
    private DataExportService dataExportService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST') or hasRole('MANAGER')")
    public ResponseEntity<byte[]> exportData(@Valid @RequestBody ExportRequest request) {
        byte[] data = dataExportService.exportData(request);
        
        String filename = generateFilename(request);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(getMediaType(request.getExportFormat()))
                .contentLength(data.length)
                .body(data);
    }
    
    private String generateFilename(ExportRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String extension = getFileExtension(request.getExportFormat());
        return "export_" + request.getDataSource() + "_" + timestamp + extension;
    }
    
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