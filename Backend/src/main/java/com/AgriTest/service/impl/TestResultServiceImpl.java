package com.AgriTest.service.impl;

import com.AgriTest.dto.TestResultRequest;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TestResult;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.repository.TestResultRepository;
import com.AgriTest.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestResultServiceImpl implements TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    @Transactional
    public TestResult createTestResult(TestResultRequest request) {
        TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + request.getTestCaseId()));

        TestResult testResult = new TestResult();
        updateTestResultFromRequest(testResult, request);
        testResult.setTestPhase(testCase.getPhases().get(0)); // Assuming first phase for now
        testResult.setCreatedBy(getCurrentUsername());
        return testResultRepository.save(testResult);
    }

    @Override
    public TestResult getTestResult(Long id) {
        return testResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test result not found with id: " + id));
    }

    @Override
    public List<TestResult> getTestResultsByTestCase(Long testCaseId) {
        return testResultRepository.findByTestPhase_TestCase_Id(testCaseId);
    }

    @Override
    public List<TestResult> getTestResultsByProduct(Long productId) {
        return testResultRepository.findByProductId(productId);
    }

    @Override
    public List<TestResult> getAllTestResults() {
        return testResultRepository.findAll();
    }

    @Override
    @Transactional
    public TestResult updateTestResult(Long id, TestResultRequest request) {
        TestResult testResult = getTestResult(id);
        updateTestResultFromRequest(testResult, request);
        testResult.setUpdatedBy(getCurrentUsername());
        return testResultRepository.save(testResult);
    }

    @Override
    @Transactional
    public void deleteTestResult(Long id) {
        testResultRepository.deleteById(id);
    }

    private void updateTestResultFromRequest(TestResult testResult, TestResultRequest request) {
        testResult.setParameterName(request.getParameterName());
        testResult.setValue(request.getValue());
        testResult.setUnit(request.getUnit());
        testResult.setNotes(request.getNotes());
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
} 