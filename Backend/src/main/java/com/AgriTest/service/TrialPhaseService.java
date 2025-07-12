package com.AgriTest.service;

import com.AgriTest.dto.TrialPhaseRequest;
<<<<<<< HEAD
import com.AgriTest.dto.TrialPhaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TrialPhaseService {
    // Core CRUD operations
    TrialPhaseResponse createTrialPhase(TrialPhaseRequest request);
    TrialPhaseResponse updateTrialPhase(Long id, TrialPhaseRequest request);
    TrialPhaseResponse getTrialPhaseById(Long id);
    void deleteTrialPhase(Long id);
    
    // Query operations
    List<TrialPhaseResponse> getAllTrialPhases();
    Page<TrialPhaseResponse> getTrialPhasesPaginated(Pageable pageable);
    List<TrialPhaseResponse> getTrialPhasesByTestCase(Long testCaseId);
    List<TrialPhaseResponse> getTrialPhasesByStatus(String status);
    
    // Status management
    TrialPhaseResponse updateTrialPhaseStatus(Long id, String status);
    
    // Bulk operations
    List<TrialPhaseResponse> createBulkTrialPhases(List<TrialPhaseRequest> requests);
    void deleteBulkTrialPhases(List<Long> ids);
=======
import com.AgriTest.model.TrialPhase;
import java.util.List;

public interface TrialPhaseService {
    TrialPhase createTrialPhase(TrialPhaseRequest request);
    TrialPhase getTrialPhase(Long id);
    List<TrialPhase> getTrialPhasesByTestCase(Long testCaseId);
    List<TrialPhase> getAllTrialPhases();
    TrialPhase updateTrialPhase(Long id, TrialPhaseRequest request);
    void deleteTrialPhase(Long id);
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589
} 