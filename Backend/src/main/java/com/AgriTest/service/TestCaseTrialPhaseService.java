// File: src/main/java/com/AgriTest/service/TestCaseTrialPhaseService.java
package com.AgriTest.service;

import com.AgriTest.dto.TestCaseTrialPhaseRequest;
import com.AgriTest.dto.TestCaseTrialPhaseResponse;

import java.util.List;

public interface TestCaseTrialPhaseService {
    TestCaseTrialPhaseResponse createTrialPhase(TestCaseTrialPhaseRequest request);
    TestCaseTrialPhaseResponse updateTrialPhase(Long id, TestCaseTrialPhaseRequest request);
    TestCaseTrialPhaseResponse getTrialPhaseById(Long id);
    List<TestCaseTrialPhaseResponse> getTrialPhasesByTestCase(Long testCaseId);
    void deleteTrialPhase(Long id);
}