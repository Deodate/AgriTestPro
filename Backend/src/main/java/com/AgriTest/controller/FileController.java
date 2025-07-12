package com.AgriTest.controller;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.service.FileStorageService;
import com.AgriTest.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<MediaFileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("testResultId") Long testResultId) {
        
        Long userId = SecurityUtils.getCurrentUserId();
        MediaFileResponse response = fileStorageService.storeFile(file, testResultId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/upload/expense-receipt/{expenseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE') or hasRole('TESTER')")
    public ResponseEntity<MediaFileResponse> uploadExpenseReceipt(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long expenseId) {
        
        Long userId = SecurityUtils.getCurrentUserId();
        MediaFileResponse response = fileStorageService.storeExpenseReceipt(file, expenseId, userId);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/upload/incident-report/{incidentReportId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<MediaFileResponse> uploadIncidentReportFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long incidentReportId) {
        
        Long userId = SecurityUtils.getCurrentUserId();
        MediaFileResponse response = fileStorageService.storeFile(
                file, incidentReportId, userId, "INCIDENT_REPORT");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/upload/announcement/{announcementId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MediaFileResponse> uploadAnnouncementFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long announcementId) {
        
        Long userId = SecurityUtils.getCurrentUserId();
        MediaFileResponse response = fileStorageService.storeFile(
                file, announcementId, userId, "ANNOUNCEMENT");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/upload-multiple/{entityType}/{entityId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<List<MediaFileResponse>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        Long userId = SecurityUtils.getCurrentUserId();
        List<MediaFileResponse> responses = Arrays.stream(files)
                .map(file -> fileStorageService.storeFile(
                        file, entityId, userId, entityType))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFileById(@PathVariable Long id, HttpServletRequest request) {
        // First get the file info to get the filename
        MediaFileResponse fileInfo = fileStorageService.getFileById(id);
        Resource resource = fileStorageService.loadFileAsResource(fileInfo.getFileName());

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping("/entities/{entityType}/{entityId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER') or hasRole('FINANCE')")
    public ResponseEntity<List<MediaFileResponse>> getFilesByEntityIdAndType(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        List<MediaFileResponse> responses = fileStorageService.getFilesByEntityIdAndType(entityId, entityType);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/expense-receipt/{expenseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE') or hasRole('TESTER')")
    public ResponseEntity<MediaFileResponse> getExpenseReceipt(@PathVariable Long expenseId) {
        MediaFileResponse receipt = fileStorageService.getExpenseReceipt(expenseId);
        return ResponseEntity.ok(receipt);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileStorageService.deleteFile(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/entities/{entityType}/{entityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFilesByEntityIdAndType(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        fileStorageService.deleteFilesByEntityIdAndType(entityId, entityType);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/expense-receipt/{expenseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FINANCE')")
    public ResponseEntity<Void> deleteExpenseReceipt(@PathVariable Long expenseId) {
        fileStorageService.deleteExpenseReceipt(expenseId);
        return ResponseEntity.noContent().build();
    }
}