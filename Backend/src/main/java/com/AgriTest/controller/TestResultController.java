package com.AgriTest.controller;

import com.AgriTest.dto.TestResultRequest;
import com.AgriTest.model.TestResult;
import com.AgriTest.service.TestResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @PostMapping
    public ResponseEntity<TestResult> createTestResult(@Valid @RequestBody TestResultRequest request) {
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
            @Valid @RequestBody TestResultRequest request) {
        return ResponseEntity.ok(testResultService.updateTestResult(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestResult(@PathVariable Long id) {
        testResultService.deleteTestResult(id);
        return ResponseEntity.ok().build();
    }
} 