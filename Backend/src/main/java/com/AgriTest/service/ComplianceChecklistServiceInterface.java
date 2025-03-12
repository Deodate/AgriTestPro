package com.AgriTest.service;

import com.AgriTest.dto.ComplianceChecklistDTO;
import com.AgriTest.dto.ChecklistItemDTO;
import com.AgriTest.model.ComplianceChecklist;

import java.time.LocalDate;
import java.util.List;

public interface ComplianceChecklistServiceInterface {
    // Existing methods from the current implementation
    ComplianceChecklistDTO createChecklist(ComplianceChecklistDTO checklistDTO);
    ComplianceChecklistDTO getChecklistById(Long id);
    List<ComplianceChecklistDTO> getAllChecklists();
    List<ComplianceChecklistDTO> getChecklistsByProductId(Long productId);
    List<ComplianceChecklistDTO> getChecklistsByDateRange(LocalDate startDate, LocalDate endDate);
    List<ComplianceChecklistDTO> getChecklistsByReviewer(String reviewerName);
    ComplianceChecklistDTO updateChecklist(Long id, ComplianceChecklistDTO checklistDTO);
    void deleteChecklist(Long id);

    // Additional methods
    List<ComplianceChecklistDTO> getChecklistsByComplianceStatus(ComplianceChecklist.ComplianceStatus status);
    List<ComplianceChecklistDTO> getChecklistsWithLowComplianceScore(double thresholdPercentage);
    ComplianceChecklistDTO addChecklistItem(Long checklistId, ChecklistItemDTO newItemDTO);
    ComplianceChecklistDTO removeChecklistItem(Long checklistId, Long itemId);
    long countChecklistsByDateRange(LocalDate startDate, LocalDate endDate);
    double calculateAverageComplianceScore(LocalDate startDate, LocalDate endDate);
}