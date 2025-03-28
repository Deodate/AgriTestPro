package com.AgriTest.service;

import com.AgriTest.dto.TrialPhaseRequest;
import com.AgriTest.model.TrialPhase;
import java.util.List;

public interface TrialPhaseService {
    TrialPhase createTrialPhase(TrialPhaseRequest request);
    TrialPhase getTrialPhase(Long id);
    List<TrialPhase> getTrialPhasesByTestCase(Long testCaseId);
    List<TrialPhase> getAllTrialPhases();
    TrialPhase updateTrialPhase(Long id, TrialPhaseRequest request);
    void deleteTrialPhase(Long id);
} 