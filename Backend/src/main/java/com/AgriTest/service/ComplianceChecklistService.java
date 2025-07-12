package com.AgriTest.service;

import com.AgriTest.dto.ComplianceChecklistRequest;
import com.AgriTest.dto.ComplianceChecklistResponse;

import java.time.LocalDate;
import java.util.List;

public interface ComplianceChecklistService {
    // Create a new compliance checklist
    ComplianceChecklistResponse createComplianceChecklist(ComplianceChecklistRequest request);
    
    // Get all compliance checklists
    List<ComplianceChecklistResponse> getAllComplianceChecklists();
    
    // Get compliance checklist by ID
    ComplianceChecklistResponse getComplianceChecklistById(Long id);
    
    // Get compliance checklists by product ID
    List<ComplianceChecklistResponse> getComplianceChecklistsByProductId(Long productId);
    
    // Get compliance checklists by reviewer name
    List<ComplianceChecklistResponse> getComplianceChecklistsByReviewerName(String reviewerName);
    
    // Get compliance checklists by date range
    List<ComplianceChecklistResponse> getComplianceChecklistsByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Get most recent compliance checklists
    List<ComplianceChecklistResponse> getMostRecentChecklists(int limit);
    
    // Update an existing compliance checklist
    ComplianceChecklistResponse updateComplianceChecklist(Long id, ComplianceChecklistRequest request);
    
    // Delete a compliance checklist
    void deleteComplianceChecklist(Long id);
}