package com.AgriTest.controller;

import com.AgriTest.dto.TestDocumentationRequest;
import com.AgriTest.dto.TestDocumentationResponse;
import com.AgriTest.service.TestDocumentationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/test-documentation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestDocumentationController {

    private final TestDocumentationService testDocumentationService;

    @Autowired
    public TestDocumentationController(TestDocumentationService testDocumentationService) {
        this.testDocumentationService = testDocumentationService;
    }

    @PostMapping
    public ResponseEntity<TestDocumentationResponse> createTestDocumentation(
            @RequestParam("testName") String testName,
            @RequestParam("testType") String testType,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("testProcedure") String testProcedure,
            @RequestParam("expectedResults") String expectedResults,
            @RequestParam(value = "actualResults", required = false) String actualResults,
            @RequestParam("testStatus") String testStatus,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments,
            Authentication authentication) {
        
        TestDocumentationRequest request = new TestDocumentationRequest();
        request.setTestName(testName);
        request.setTestType(testType);
        request.setDescription(description);
        request.setTestProcedure(testProcedure);
        request.setExpectedResults(expectedResults);
        request.setActualResults(actualResults);
        request.setTestStatus(testStatus);

        TestDocumentationResponse response = testDocumentationService.createTestDocumentation(
            request, attachments, authentication.getName());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestDocumentationResponse> getTestDocumentation(@PathVariable Long id) {
        return ResponseEntity.ok(testDocumentationService.getTestDocumentationById(id));
    }

    @GetMapping
    public ResponseEntity<List<TestDocumentationResponse>> getAllTestDocumentations() {
        return ResponseEntity.ok(testDocumentationService.getAllTestDocumentations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestDocumentationResponse> updateTestDocumentation(
            @PathVariable Long id,
            @Valid @RequestBody TestDocumentationRequest request) {
        return ResponseEntity.ok(testDocumentationService.updateTestDocumentation(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestDocumentation(@PathVariable Long id) {
        testDocumentationService.deleteTestDocumentation(id);
        return ResponseEntity.ok().build();
    }
} 