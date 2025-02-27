package com.AgriTest.service;

import com.AgriTest.dto.*;

import java.util.List;
import java.util.Optional;

public interface TestCaseService {
    List<TestCaseResponse> getAllTestCases();
    
    Optional<TestCaseResponse> getTestCaseById(Long id);
    
    TestCaseResponse createTestCase(TestCaseRequest testCaseRequest, Long userId);
    
    TestCaseResponse updateTestCase(Long id, TestCaseRequest testCaseRequest);
    
    void deleteTestCase(Long id);
    
    List<TestCaseResponse> getTestCasesByProduct(Long productId);
    
    List<TestCaseResponse> getTestCasesByStatus(String status);
    
    List<TestCaseResponse> getTestCasesByUser(Long userId);
    
    TestPhaseResponse addTestPhase(Long testCaseId, TestPhaseRequest testPhaseRequest);
    
    TestResultResponse addTestResult(Long phaseId, TestResultRequest testResultRequest, Long userId);
}