// File: src/main/java/com/AgriTest/controller/FileUploadController.java
package com.AgriTest.controller;

import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.exception.FileStorageException;
import com.AgriTest.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    // Maximum file size (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // Allowed file types
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
        "image/jpeg", 
        "image/png", 
        "image/gif", 
        "application/pdf", 
        "application/msword", 
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "video/mp4"
    );

    @Autowired
    private FileStorageService fileStorageService;

    // Changed from "/upload" to "/upload-media" to avoid endpoint conflict
    @PostMapping("/upload-media")
    public ResponseEntity<MediaFileResponse> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("testResultId") Long testResultId
    ) {
        try {
            // Validate file size
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new FileStorageException("File size exceeds maximum limit of 10MB");
            }

            // Validate file type
            if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
                throw new FileStorageException("File type not allowed. Supported types: " + 
                    String.join(", ", ALLOWED_FILE_TYPES));
            }

            // Extract user ID
            Long userId = extractUserId();

            // Store file
            MediaFileResponse response = fileStorageService.storeFile(file, testResultId, userId);
            
            logger.info("File uploaded successfully: {} for Test Result ID: {}", 
                file.getOriginalFilename(), testResultId);
            
            return ResponseEntity.ok(response);
        } catch (FileStorageException e) {
            logger.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/multiple-upload")
    public ResponseEntity<List<MediaFileResponse>> uploadMultipleFiles(
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("testResultId") Long testResultId
    ) {
        try {
            List<MediaFileResponse> responses = Arrays.stream(files)
                .map(file -> {
                    try {
                        // Validate file size
                        if (file.getSize() > MAX_FILE_SIZE) {
                            throw new FileStorageException("File size exceeds maximum limit of 10MB");
                        }

                        // Validate file type
                        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
                            throw new FileStorageException("File type not allowed. Supported types: " + 
                                String.join(", ", ALLOWED_FILE_TYPES));
                        }

                        // Extract user ID
                        Long userId = extractUserId();

                        // Store file
                        return fileStorageService.storeFile(file, testResultId, userId);
                    } catch (FileStorageException e) {
                        logger.error("File upload failed: {}", file.getOriginalFilename(), e);
                        return null;
                    }
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());

            logger.info("Uploaded {} files for Test Result ID: {}", responses.size(), testResultId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Multiple file upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Changed from "/download/{fileName:.+}" to "/download-media/{fileName:.+}" to avoid endpoint conflict
    @GetMapping("/download-media/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
        @PathVariable String fileName, 
        HttpServletRequest request
    ) {
        try {
            // Load file as resource
            Resource resource = fileStorageService.loadFileAsResource(fileName);
            
            // Determine content type
            String contentType = determineContentType(request, resource);
            
            logger.info("Downloading file: {}", fileName);
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (Exception e) {
            logger.error("File download failed", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Changed from "/{id}" to "/delete-media/{id}" to avoid endpoint conflict
    @DeleteMapping("/delete-media/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        try {
            fileStorageService.deleteFile(id);
            
            logger.info("File deleted successfully: ID {}", id);
            
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("File deletion failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // User ID extraction method
    private Long extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        // Handle different authentication scenarios
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            // If using UserDetails, you might need to map to your user ID
            // This depends on your specific UserDetails implementation
            return Long.parseLong(((UserDetails) principal).getUsername());
        } else if (principal instanceof String) {
            return Long.parseLong((String) principal);
        }

        // Fallback - this should be replaced with your actual user ID extraction logic
        return 1L;
    }

    // Content type determination method
    private String determineContentType(HttpServletRequest request, Resource resource) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.warn("Could not determine file type. Using default.");
            contentType = "application/octet-stream";
        }
        
        // Fallback to default if no content type found
        return contentType == null ? "application/octet-stream" : contentType;
    }
}