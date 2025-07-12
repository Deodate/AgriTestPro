package com.AgriTest.controller;

import com.AgriTest.dto.TestResultRequest;
import com.AgriTest.model.TestResult;
import com.AgriTest.service.TestResultService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testresults")
public class TestResultController {

    private static final Logger logger = LoggerFactory.getLogger(TestResultController.class);

    @Autowired
    private TestResultService testResultService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<TestResult> createTestResult(@Valid @ModelAttribute TestResultRequest request) {
        logger.info("Received TestResultRequest: {}", request);
        return ResponseEntity.ok(testResultService.createTestResult(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestResult> getTestResult(@PathVariable Long id) {
        return ResponseEntity.ok(testResultService.getTestResult(id));
    }

    @GetMapping("/test-case/{testCaseId}")
    public ResponseEntity<List<TestResult>> getTestResultsByTestCase(@PathVariable Long testCaseId) {
        return ResponseEntity.ok(testResultService.getTestResultsByTestCase(testCaseId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TestResult>> getTestResultsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(testResultService.getTestResultsByProduct(productId));
    }

    @GetMapping
    public ResponseEntity<List<TestResult>> getAllTestResults() {
        return ResponseEntity.ok(testResultService.getAllTestResults());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestResult> updateTestResult(
            @PathVariable Long id,
            @Valid @ModelAttribute TestResultRequest request) {
        return ResponseEntity.ok(testResultService.updateTestResult(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestResult(@PathVariable Long id) {
        testResultService.deleteTestResult(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/failed/count")
    public ResponseEntity<Long> countFailedTestResults() {
        logger.info("Received request to count failed test results");
        Long failedCount = testResultService.countFailedTestResults();
        logger.info("Returning failed test results count: {}", failedCount);
        return ResponseEntity.ok(failedCount);
    }
} 