package com.AgriTest.service.impl;

import com.AgriTest.dto.TrialPhaseRequest;
import com.AgriTest.model.TestCase;
import com.AgriTest.model.TrialPhase;
import com.AgriTest.repository.TestCaseRepository;
import com.AgriTest.repository.TrialPhaseRepository;
import com.AgriTest.service.TrialPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrialPhaseServiceImpl2 implements TrialPhaseService {

    @Autowired
    private TrialPhaseRepository trialPhaseRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    @Transactional
    public TrialPhase createTrialPhase(TrialPhaseRequest request) {
        TestCase testCase = testCaseRepository.findById(request.getTestCaseId())
                .orElseThrow(() -> new RuntimeException("Test case not found with id: " + request.getTestCaseId()));

        TrialPhase trialPhase = new TrialPhase();
        updateTrialPhaseFromRequest(trialPhase, request);
        trialPhase.setTestCase(testCase);
        return trialPhaseRepository.save(trialPhase);
    }

    @Override
    public TrialPhase getTrialPhase(Long id) {
        return trialPhaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trial phase not found with id: " + id));
    }

    @Override
    public List<TrialPhase> getTrialPhasesByTestCase(Long testCaseId) {
        return trialPhaseRepository.findByTestCaseId(testCaseId);
    }

    @Override
    public List<TrialPhase> getAllTrialPhases() {
        return trialPhaseRepository.findAll();
    }

    @Override
    @Transactional
    public TrialPhase updateTrialPhase(Long id, TrialPhaseRequest request) {
        TrialPhase trialPhase = getTrialPhase(id);
        updateTrialPhaseFromRequest(trialPhase, request);
        return trialPhaseRepository.save(trialPhase);
    }

    @Override
    @Transactional
    public void deleteTrialPhase(Long id) {
        trialPhaseRepository.deleteById(id);
    }

    private void updateTrialPhaseFromRequest(TrialPhase trialPhase, TrialPhaseRequest request) {
        trialPhase.setPhaseName(request.getPhaseName());
        trialPhase.setDateOfPhase(request.getDateOfPhase());
        trialPhase.setObservations(request.getObservations());
        trialPhase.setTestDataEntry(request.getTestDataEntry());
        trialPhase.setWeatherTemperature(request.getWeatherTemperature());
        trialPhase.setWeatherHumidity(request.getWeatherHumidity());
        trialPhase.setWeatherRainfall(request.getWeatherRainfall());
        trialPhase.setAdditionalComments(request.getAdditionalComments());
    }
} 