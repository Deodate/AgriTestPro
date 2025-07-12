// File: src/main/java/com/AgriTest/controller/TestCaseController.java
package com.AgriTest.controller;

import com.AgriTest.dto.*;
import com.AgriTest.service.TestCaseService;
import com.AgriTest.util.SecurityUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
public class TestCaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseController.class);

    @Autowired
    private TestCaseService testCaseService;

    @GetMapping
    public List<TestCaseResponse> getAllTestCases() {
        List<TestCaseResponse> testCases = testCaseService.getAllTestCases();
        logger.info("Returning {} test cases", testCases.size());
        return testCases;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCaseResponse> getTestCaseById(@PathVariable Long id) {
        return testCaseService.getTestCaseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public TestCaseResponse createTestCase(@Valid @RequestBody TestCaseRequest testCaseRequest) {
        Long userId = SecurityUtils.getCurrentUserId();
        return testCaseService.createTestCase(testCaseRequest, userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public ResponseEntity<TestCaseResponse> updateTestCase(@PathVariable Long id, @Valid @RequestBody TestCaseRequest testCaseRequest) {
        return ResponseEntity.ok(testCaseService.updateTestCase(id, testCaseRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        testCaseService.deleteTestCase(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product/{productId}")
    public List<TestCaseResponse> getTestCasesByProduct(@PathVariable Long productId) {
        return testCaseService.getTestCasesByProduct(productId);
    }

    @GetMapping("/status/{status}")
    public List<TestCaseResponse> getTestCasesByStatus(@PathVariable String status) {
        return testCaseService.getTestCasesByStatus(status);
    }

    @GetMapping("/user")
    public List<TestCaseResponse> getMyTestCases() {
        Long userId = SecurityUtils.getCurrentUserId();
        return testCaseService.getTestCasesByUser(userId);
    }

    @PostMapping("/{id}/phases")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public TestPhaseResponse addTestPhase(@PathVariable Long id, @Valid @RequestBody TestPhaseRequest testPhaseRequest) {
        return testCaseService.addTestPhase(id, testPhaseRequest);
    }

    @PostMapping("/phases/{phaseId}/results")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TESTER')")
    public TestResultResponse addTestResult(@PathVariable Long phaseId, @Valid @RequestBody TestResultRequest testResultRequest) {
        Long userId = SecurityUtils.getCurrentUserId();
        return testCaseService.addTestResult(phaseId, testResultRequest, userId);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalTestCaseCount() {
        long count = testCaseService.getTotalTestCaseCount();
        return ResponseEntity.ok(count);
    }
}