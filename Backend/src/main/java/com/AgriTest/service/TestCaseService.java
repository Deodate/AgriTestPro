package com.AgriTest.service;

import com.AgriTest.dto.*;

import java.util.List;
import java.util.Optional;

public interface TestCaseService {
    /**
     * Get all test cases
     */
    List<TestCaseResponse> getAllTestCases();
    
    /**
     * Get a test case by ID
     */
    Optional<TestCaseResponse> getTestCaseById(Long id);
    
    /**
     * Create a new test case
     */
    TestCaseResponse createTestCase(TestCaseRequest testCaseRequest, Long userId);
    
    /**
     * Update an existing test case
     */
    TestCaseResponse updateTestCase(Long id, TestCaseRequest testCaseRequest);
    
    /**
     * Delete a test case
     */
    void deleteTestCase(Long id);
    
    /**
     * Get test cases by product ID
     */
    List<TestCaseResponse> getTestCasesByProduct(Long productId);
    
    /**
     * Get test cases by status
     */
    List<TestCaseResponse> getTestCasesByStatus(String status);
    
    /**
     * Get test cases by user ID
     */
    List<TestCaseResponse> getTestCasesByUser(Long userId);
    
    /**
     * Add a phase to a test case
     */
    TestPhaseResponse addTestPhase(Long testCaseId, TestPhaseRequest testPhaseRequest);
    
    /**
     * Add a result to a test phase
     */
    TestResultResponse addTestResult(Long phaseId, TestResultRequest testResultRequest, Long userId);
    
    /**
     * Get test cases by specific IDs
     * @param testCaseIds List of test case IDs to retrieve
     * @return List of test case responses
     */
    List<TestCaseResponse> getTestCasesByIds(List<Long> testCaseIds);
    
    /**
     * Get test results for a specific test case
     * @param testCaseId ID of the test case to retrieve results for
     * @return List of test result responses
     */
    List<TestResultResponse> getTestResultsByTestCase(Long testCaseId);
}