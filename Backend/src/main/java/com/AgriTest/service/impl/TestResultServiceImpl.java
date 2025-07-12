package com.AgriTest.service.impl;

import com.AgriTest.dto.TestResultRequest;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TestResult;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.repository.TestResultRepository;
import com.AgriTest.service.TestResultService;
import com.AgriTest.service.FileStorageService;
import com.AgriTest.dto.MediaFileResponse;
import com.AgriTest.model.MediaFile;
import com.AgriTest.mapper.MediaFileMapper;
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

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MediaFileMapper mediaFileMapper;

    @Override
    @Transactional
    public TestResult createTestResult(TestResultRequest request) {
        TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + request.getTestCaseId()));

        // Ensure the test case has at least one phase
        if (testCase.getPhases() == null || testCase.getPhases().isEmpty()) {
            throw new RuntimeException("Selected Test Case has no phases defined.");
        }

        TestResult testResult = new TestResult();
        updateTestResultFromRequest(testResult, request);
        testResult.setTestPhase(testCase.getPhases().get(0)); // Assuming first phase for now
        testResult.setCreatedBy(getCurrentUsername());

        // Save test result first to get ID
        TestResult savedTestResult = testResultRepository.save(testResult);

        // Handle file attachments
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            Long userId = getCurrentUserId(); // Assuming a method to get current user ID
            request.getAttachments().forEach(file -> {
                if (!file.isEmpty()) {
                    fileStorageService.storeFile(file, savedTestResult.getId(), userId, "TEST_RESULT");
                }
            });
        }

        return savedTestResult;
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

        // Handle file attachments for update
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
             Long userId = getCurrentUserId(); // Assuming a method to get current user ID
            // Option 1: Delete existing files before adding new ones
            // fileStorageService.deleteFilesByEntityIdAndType(testResult.getId(), "TEST_RESULT");

            // Option 2: Just add new files (existing files remain)
            request.getAttachments().forEach(file -> {
                 if (!file.isEmpty()) {
                    fileStorageService.storeFile(file, testResult.getId(), userId, "TEST_RESULT");
                 }
            });
        }

        return testResultRepository.save(testResult);
    }

    @Override
    @Transactional
    public void deleteTestResult(Long id) {
        // Also delete associated files
        fileStorageService.deleteFilesByEntityIdAndType(id, "TEST_RESULT");
        testResultRepository.deleteById(id);
    }

    @Override
    public Long countFailedTestResults() {
        return testResultRepository.count();
    }

    private void updateTestResultFromRequest(TestResult testResult, TestResultRequest request) {
        testResult.setParameterName(request.getParameterName());
        testResult.setValue(request.getValue());
        testResult.setUnit(request.getUnit());
        testResult.setNotes(request.getNotes());
        testResult.setProductId(request.getProductId());
        testResult.setTrialPhase(request.getTrialPhase());
        testResult.setGrowthRate(request.getGrowthRate());
        testResult.setYield(request.getYield());
        testResult.setPestResistance(request.getPestResistance());
        testResult.setFinalVerdict(request.getFinalVerdict());
        testResult.setRecommendations(request.getRecommendations());
        testResult.setApprovedBy(request.getApprovedBy());
        testResult.setDateOfApproval(request.getDateOfApproval());
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Helper method to get current user ID (needs implementation based on your auth system)
    private Long getCurrentUserId() {
        // Placeholder: Replace with actual logic to get the current user's ID
        // For example, if user ID is stored in JWT claims or a UserDetails object
        // You might need to inject a UserRepository or a custom UserDetails service
        // For now, returning a placeholder value or throwing an error
        // throw new UnsupportedOperationException("getCurrentUserId not implemented");
         return 1L; // Example: return a default or test user ID for now
    }
} 