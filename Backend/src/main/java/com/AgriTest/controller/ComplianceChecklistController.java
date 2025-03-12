package com.AgriTest.controller;

import com.AgriTest.dto.ComplianceChecklistDTO;
import com.AgriTest.dto.ChecklistItemDTO;
import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.service.ComplianceChecklistServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/compliance")
public class ComplianceChecklistController {

    private final ComplianceChecklistServiceInterface complianceChecklistService;

    @Autowired
    public ComplianceChecklistController(ComplianceChecklistServiceInterface complianceChecklistService) {
        this.complianceChecklistService = complianceChecklistService;
    }

    // Existing endpoints
    @PostMapping
    public ResponseEntity<ComplianceChecklistDTO> createChecklist(@Valid @RequestBody ComplianceChecklistDTO checklistDTO) {
        ComplianceChecklistDTO createdChecklist = complianceChecklistService.createChecklist(checklistDTO);
        return new ResponseEntity<>(createdChecklist, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplianceChecklistDTO> getChecklistById(@PathVariable Long id) {
        ComplianceChecklistDTO checklist = complianceChecklistService.getChecklistById(id);
        return ResponseEntity.ok(checklist);
    }

    @GetMapping
    public ResponseEntity<List<ComplianceChecklistDTO>> getAllChecklists() {
        List<ComplianceChecklistDTO> checklists = complianceChecklistService.getAllChecklists();
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ComplianceChecklistDTO>> getChecklistsByProductId(@PathVariable Long productId) {
        List<ComplianceChecklistDTO> checklists = complianceChecklistService.getChecklistsByProductId(productId);
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ComplianceChecklistDTO>> getChecklistsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ComplianceChecklistDTO> checklists = complianceChecklistService.getChecklistsByDateRange(startDate, endDate);
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/reviewer/{reviewerName}")
    public ResponseEntity<List<ComplianceChecklistDTO>> getChecklistsByReviewer(@PathVariable String reviewerName) {
        List<ComplianceChecklistDTO> checklists = complianceChecklistService.getChecklistsByReviewer(reviewerName);
        return ResponseEntity.ok(checklists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplianceChecklistDTO> updateChecklist(
            @PathVariable Long id,
            @Valid @RequestBody ComplianceChecklistDTO checklistDTO) {
        ComplianceChecklistDTO updatedChecklist = complianceChecklistService.updateChecklist(id, checklistDTO);
        return ResponseEntity.ok(updatedChecklist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChecklist(@PathVariable Long id) {
        complianceChecklistService.deleteChecklist(id);
        return ResponseEntity.noContent().build();
    }

    // New endpoints for additional service methods
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ComplianceChecklistDTO>> getChecklistsByComplianceStatus(
            @PathVariable ComplianceChecklist.ComplianceStatus status) {
        List<ComplianceChecklistDTO> checklists = complianceChecklistService.getChecklistsByComplianceStatus(status);
        return ResponseEntity.ok(checklists);
    }

    @GetMapping("/low-compliance")
    public ResponseEntity<List<ComplianceChecklistDTO>> getChecklistsWithLowComplianceScore(
            @RequestParam(defaultValue = "70.0") double thresholdPercentage) {
        List<ComplianceChecklistDTO> checklists = 
            complianceChecklistService.getChecklistsWithLowComplianceScore(thresholdPercentage);
        return ResponseEntity.ok(checklists);
    }

    @PostMapping("/{checklistId}/items")
    public ResponseEntity<ComplianceChecklistDTO> addChecklistItem(
            @PathVariable Long checklistId,
            @Valid @RequestBody ChecklistItemDTO itemDTO) {
        ComplianceChecklistDTO updatedChecklist = 
            complianceChecklistService.addChecklistItem(checklistId, itemDTO);
        return ResponseEntity.ok(updatedChecklist);
    }

    @DeleteMapping("/{checklistId}/items/{itemId}")
    public ResponseEntity<ComplianceChecklistDTO> removeChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId) {
        ComplianceChecklistDTO updatedChecklist = 
            complianceChecklistService.removeChecklistItem(checklistId, itemId);
        return ResponseEntity.ok(updatedChecklist);
    }

    @GetMapping("/count/date-range")
    public ResponseEntity<Map<String, Long>> countChecklistsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        long count = complianceChecklistService.countChecklistsByDateRange(startDate, endDate);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/average-compliance/date-range")
    public ResponseEntity<Map<String, Double>> calculateAverageComplianceScore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        double averageScore = complianceChecklistService.calculateAverageComplianceScore(startDate, endDate);
        Map<String, Double> response = new HashMap<>();
        response.put("averageComplianceScore", averageScore);
        return ResponseEntity.ok(response);
    }
}