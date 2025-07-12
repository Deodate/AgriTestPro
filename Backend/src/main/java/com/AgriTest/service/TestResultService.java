package com.AgriTest.service;

import com.AgriTest.dto.TestResultRequest;
import com.AgriTest.model.TestResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestResultService {
    TestResult createTestResult(TestResultRequest request);
    TestResult getTestResult(Long id);
    List<TestResult> getTestResultsByTestCase(Long testCaseId);
    List<TestResult> getTestResultsByProduct(Long productId);
    List<TestResult> getAllTestResults();
    TestResult updateTestResult(Long id, TestResultRequest request);
    void deleteTestResult(Long id);
    Long countFailedTestResults();
} 