package com.AgriTest.controller;

import com.AgriTest.service.EvidenceUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

@RestController
@RequestMapping("/api/evidence")
public class EvidenceUploadController {

    private final EvidenceUploadService evidenceUploadService;

    @Autowired
    public EvidenceUploadController(EvidenceUploadService evidenceUploadService) {
        this.evidenceUploadService = evidenceUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadEvidence(
            @RequestParam("file") MultipartFile file,
            @RequestParam("testCaseId") Long testCaseId,
            @RequestParam("mediaType") String mediaType,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "takenBy", required = false) String takenBy,
            @RequestParam(value = "dateCaptured", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateCaptured // Assuming date format can be parsed
    ) {
        try {
            // Call service to handle the upload and metadata saving
            evidenceUploadService.uploadEvidence(file, testCaseId, mediaType, description, takenBy, dateCaptured);
            return ResponseEntity.status(HttpStatus.OK).body("Evidence uploaded successfully!");
        } catch (Exception e) {
            // Log the error on the server side
            e.printStackTrace(); // Or use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload evidence: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<com.AgriTest.model.EvidenceUpload>> getAllEvidence() {
        try {
            List<com.AgriTest.model.EvidenceUpload> evidenceList = evidenceUploadService.getAllEvidence();
            return ResponseEntity.ok(evidenceList);
        } catch (Exception e) {
            // Log the error
            e.printStackTrace(); // Or use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.AgriTest.model.EvidenceUpload> getEvidenceById(@PathVariable Long id) {
        try {
            com.AgriTest.model.EvidenceUpload evidence = evidenceUploadService.getEvidenceById(id);
            return ResponseEntity.ok(evidence);
        } catch (Exception e) {
            // Log the error
            e.printStackTrace(); // Or use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvidence(@PathVariable Long id) {
        try {
            evidenceUploadService.deleteEvidence(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the error
            e.printStackTrace(); // Or use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvidence(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("testCaseId") Long testCaseId,
            @RequestParam("mediaType") String mediaType,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "takenBy", required = false) String takenBy,
            @RequestParam(value = "dateCaptured", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateCaptured
    ) {
        try {
            evidenceUploadService.updateEvidence(id, file, testCaseId, mediaType, description, takenBy, dateCaptured);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the error
            e.printStackTrace(); // Or use a logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add other endpoints for retrieving evidence if needed
} 