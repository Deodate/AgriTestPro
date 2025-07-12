package com.AgriTest.controller;

import com.AgriTest.dto.TrialPhaseRequest;
import com.AgriTest.model.TrialPhase;
import com.AgriTest.service.TrialPhaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trial-phases")
public class TrialPhaseController {

    @Autowired
    private TrialPhaseService trialPhaseService;

    @PostMapping
    public ResponseEntity<TrialPhase> createTrialPhase(@Valid @RequestBody TrialPhaseRequest request) {
        return ResponseEntity.ok(trialPhaseService.createTrialPhase(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialPhase> getTrialPhase(@PathVariable Long id) {
        return ResponseEntity.ok(trialPhaseService.getTrialPhase(id));
    }

    @GetMapping("/test-case/{testCaseId}")
    public ResponseEntity<List<TrialPhase>> getTrialPhasesByTestCase(@PathVariable Long testCaseId) {
        return ResponseEntity.ok(trialPhaseService.getTrialPhasesByTestCase(testCaseId));
    }

    @GetMapping
    public ResponseEntity<List<TrialPhase>> getAllTrialPhases() {
        return ResponseEntity.ok(trialPhaseService.getAllTrialPhases());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialPhase> updateTrialPhase(
            @PathVariable Long id,
            @Valid @RequestBody TrialPhaseRequest request) {
        return ResponseEntity.ok(trialPhaseService.updateTrialPhase(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrialPhase(@PathVariable Long id) {
        trialPhaseService.deleteTrialPhase(id);
        return ResponseEntity.ok().build();
    }
} 