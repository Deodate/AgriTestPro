// File: src/main/java/com/AgriTest/controller/TestCaseTrialPhaseController.java
package com.AgriTest.controller;

import com.AgriTest.dto.TestCaseTrialPhaseRequest;
import com.AgriTest.dto.TestCaseTrialPhaseResponse;
import com.AgriTest.service.TestCaseTrialPhaseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Add imports for file handling and media type
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/test-case-trial-phases")
public class TestCaseTrialPhaseController {
    private static final Logger logger = LoggerFactory.getLogger(TestCaseTrialPhaseController.class);

    @Autowired
    private TestCaseTrialPhaseService service;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<TestCaseTrialPhaseResponse> createTrialPhase(
        @Valid @ModelAttribute TestCaseTrialPhaseRequest request
    ) {
        try {
            TestCaseTrialPhaseResponse response = service.createTrialPhase(request);
            logger.info("Trial phase created successfully for Test Case ID: {}", request.getTestCaseId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating trial phase", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<TestCaseTrialPhaseResponse> updateTrialPhase(
        @PathVariable Long id,
        @Valid @ModelAttribute TestCaseTrialPhaseRequest request
    ) {
        try {
            TestCaseTrialPhaseResponse response = service.updateTrialPhase(id, request);
            logger.info("Trial phase updated successfully: ID {}", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating trial phase", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseTrialPhaseResponse> getTrialPhaseById(@PathVariable Long id) {
        try {
            TestCaseTrialPhaseResponse response = service.getTrialPhaseById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error retrieving trial phase", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/test-case/{testCaseId}")
    public ResponseEntity<List<TestCaseTrialPhaseResponse>> getTrialPhasesByTestCase(
        @PathVariable Long testCaseId
    ) {
        try {
            List<TestCaseTrialPhaseResponse> responses = service.getTrialPhasesByTestCase(testCaseId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error retrieving trial phases for test case", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TestCaseTrialPhaseResponse>> getAllTrialPhases() {
        logger.info("Received request to fetch all Trial Phases.");
        try {
            List<TestCaseTrialPhaseResponse> responses = service.getAllTrialPhases();
            logger.info("Fetched {} trial phases.", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching all trial phases", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrialPhase(@PathVariable Long id) {
        try {
            service.deleteTrialPhase(id);
            logger.info("Trial phase deleted successfully: ID {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting trial phase", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveTrialPhaseImage(@PathVariable String filename) {
        try {
            // ** IMPORTANT: Update this path to the actual directory where your trial phase images are stored **
            Path file = Paths.get("uploads/trial-phases/images").resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                // Determine content type dynamically if possible, or set a default
                String contentType = "application/octet-stream"; // Default
                try {
                    contentType = java.nio.file.Files.probeContentType(file);
                    if (contentType == null) {
                         contentType = "application/octet-stream";
                    }
                } catch (java.io.IOException e) {
                    logger.warn("Could not determine file type for {}", filename, e);
                }

                logger.info("Serving image file: {}", filename);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                logger.warn("Image file not found or not readable: {}", filename);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error serving image file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}