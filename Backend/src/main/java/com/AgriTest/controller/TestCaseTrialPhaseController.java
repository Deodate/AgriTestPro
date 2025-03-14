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
}