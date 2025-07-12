package com.AgriTest.controller;

import com.AgriTest.dto.TrialPhaseRequest;
import com.AgriTest.dto.TrialPhaseResponse;
import com.AgriTest.service.TrialPhaseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trial-phases")
@PreAuthorize("isAuthenticated()")
public class TrialPhaseController {
    private static final Logger logger = LoggerFactory.getLogger(TrialPhaseController.class);

    @Autowired
    private TrialPhaseService trialPhaseService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<TrialPhaseResponse> createTrialPhase(
            @Valid @ModelAttribute TrialPhaseRequest request) {
        logger.info("Creating new trial phase for test case: {}", request.getTestCaseId());
        return new ResponseEntity<>(trialPhaseService.createTrialPhase(request), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TrialPhaseResponse>> createBulkTrialPhases(
            @Valid @RequestBody List<TrialPhaseRequest> requests) {
        logger.info("Creating {} trial phases in bulk", requests.size());
        return new ResponseEntity<>(trialPhaseService.createBulkTrialPhases(requests), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public ResponseEntity<TrialPhaseResponse> getTrialPhase(@PathVariable Long id) {
        logger.info("Fetching trial phase: {}", id);
        return ResponseEntity.ok(trialPhaseService.getTrialPhaseById(id));
    }

    @GetMapping("/test-case/{testCaseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public ResponseEntity<List<TrialPhaseResponse>> getTrialPhasesByTestCase(
            @PathVariable Long testCaseId) {
        logger.info("Fetching trial phases for test case: {}", testCaseId);
        return ResponseEntity.ok(trialPhaseService.getTrialPhasesByTestCase(testCaseId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public ResponseEntity<List<TrialPhaseResponse>> getAllTrialPhases() {
        logger.info("Fetching all trial phases");
        return ResponseEntity.ok(trialPhaseService.getAllTrialPhases());
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public ResponseEntity<Page<TrialPhaseResponse>> getTrialPhasesPaginated(Pageable pageable) {
        logger.info("Fetching paginated trial phases");
        return ResponseEntity.ok(trialPhaseService.getTrialPhasesPaginated(pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TESTER', 'USER')")
    public ResponseEntity<List<TrialPhaseResponse>> getTrialPhasesByStatus(
            @PathVariable String status) {
        logger.info("Fetching trial phases with status: {}", status);
        return ResponseEntity.ok(trialPhaseService.getTrialPhasesByStatus(status));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<TrialPhaseResponse> updateTrialPhase(
            @PathVariable Long id,
            @Valid @ModelAttribute TrialPhaseRequest request) {
        logger.info("Updating trial phase: {}", id);
        return ResponseEntity.ok(trialPhaseService.updateTrialPhase(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<TrialPhaseResponse> updateTrialPhaseStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        logger.info("Updating status of trial phase {} to: {}", id, status);
        return ResponseEntity.ok(trialPhaseService.updateTrialPhaseStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTrialPhase(@PathVariable Long id) {
        logger.info("Deleting trial phase: {}", id);
        trialPhaseService.deleteTrialPhase(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBulkTrialPhases(@RequestBody List<Long> ids) {
        logger.info("Deleting {} trial phases in bulk", ids.size());
        trialPhaseService.deleteBulkTrialPhases(ids);
        return ResponseEntity.ok().build();
    }
} 