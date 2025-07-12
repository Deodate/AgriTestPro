package com.AgriTest.service;

import com.AgriTest.dto.TrialPhaseRequest;
import com.AgriTest.dto.TestCaseTrialPhaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TrialPhaseService {
    // Core CRUD operations
    TestCaseTrialPhaseResponse createTrialPhase(TrialPhaseRequest request);
    TestCaseTrialPhaseResponse updateTrialPhase(Long id, TrialPhaseRequest request);
    TestCaseTrialPhaseResponse getTrialPhaseById(Long id);
    void deleteTrialPhase(Long id);
    
    // Query operations
    List<TestCaseTrialPhaseResponse> getAllTrialPhases();
    Page<TestCaseTrialPhaseResponse> getTrialPhasesPaginated(Pageable pageable);
    List<TestCaseTrialPhaseResponse> getTrialPhasesByTestCase(Long testCaseId);
    List<TestCaseTrialPhaseResponse> getTrialPhasesByStatus(String status);
    
    // Status management
    TestCaseTrialPhaseResponse updateTrialPhaseStatus(Long id, String status);
    
    // Bulk operations
    List<TestCaseTrialPhaseResponse> createBulkTrialPhases(List<TrialPhaseRequest> requests);
    void deleteBulkTrialPhases(List<Long> ids);
} 