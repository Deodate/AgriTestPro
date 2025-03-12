package com.AgriTest.service.impl;

import com.AgriTest.dto.ComplianceChecklistDTO;
import com.AgriTest.dto.ChecklistItemDTO;
import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.model.ChecklistItem;
import com.AgriTest.repository.ComplianceChecklistRepository;
import com.AgriTest.service.ComplianceChecklistServiceInterface;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.ComplianceChecklistMapper;
import com.AgriTest.util.ComplianceChecklistUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplianceChecklistServiceImpl implements ComplianceChecklistServiceInterface {

    private final ComplianceChecklistRepository complianceChecklistRepository;
    private final ComplianceChecklistMapper checklistMapper;

    @Autowired
    public ComplianceChecklistServiceImpl(
        ComplianceChecklistRepository complianceChecklistRepository,
        ComplianceChecklistMapper checklistMapper
    ) {
        this.complianceChecklistRepository = complianceChecklistRepository;
        this.checklistMapper = checklistMapper;
    }

    // Existing methods from the previous implementation
    @Override
    @Transactional
    public ComplianceChecklistDTO createChecklist(ComplianceChecklistDTO checklistDTO) {
        ComplianceChecklist checklist = checklistMapper.toEntity(checklistDTO);
        updateOverallStatus(checklist);
        ComplianceChecklist savedChecklist = complianceChecklistRepository.save(checklist);
        return checklistMapper.toDTO(savedChecklist);
    }

    @Override
    @Transactional(readOnly = true)
    public ComplianceChecklistDTO getChecklistById(Long id) {
        ComplianceChecklist checklist = complianceChecklistRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        return checklistMapper.toDTO(checklist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistDTO> getAllChecklists() {
        return complianceChecklistRepository.findAll().stream()
            .map(checklistMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistDTO> getChecklistsByProductId(Long productId) {
        return complianceChecklistRepository.findByProductId(productId).stream()
            .map(checklistMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistDTO> getChecklistsByDateRange(LocalDate startDate, LocalDate endDate) {
        return complianceChecklistRepository.findByReviewDateBetween(startDate, endDate).stream()
            .map(checklistMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistDTO> getChecklistsByReviewer(String reviewerName) {
        return complianceChecklistRepository.findByReviewerName(reviewerName).stream()
            .map(checklistMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ComplianceChecklistDTO updateChecklist(Long id, ComplianceChecklistDTO checklistDTO) {
        ComplianceChecklist existingChecklist = complianceChecklistRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        
        // Update fields
        existingChecklist.setProductId(checklistDTO.getProductId());
        existingChecklist.setReviewerName(checklistDTO.getReviewerName());
        existingChecklist.setReviewDate(checklistDTO.getReviewDate());
        existingChecklist.setOverallComments(checklistDTO.getOverallComments());
        
        // Clear and update checklist items
        existingChecklist.getChecklistItems().clear();
        List<ChecklistItem> newItems = checklistDTO.getChecklistItems().stream()
            .map(checklistMapper::toChecklistItemEntity)
            .collect(Collectors.toList());
        existingChecklist.getChecklistItems().addAll(newItems);
        
        // Automatically determine overall status based on checklist items
        updateOverallStatus(existingChecklist);
        
        ComplianceChecklist updatedChecklist = complianceChecklistRepository.save(existingChecklist);
        return checklistMapper.toDTO(updatedChecklist);
    }

    @Override
    @Transactional
    public void deleteChecklist(Long id) {
        ComplianceChecklist checklist = complianceChecklistRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        complianceChecklistRepository.delete(checklist);
    }

    // New methods from the service interface
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistDTO> getChecklistsByComplianceStatus(ComplianceChecklist.ComplianceStatus status) {
        return complianceChecklistRepository.findByOverallStatus(status).stream()
            .map(checklistMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistDTO> getChecklistsWithLowComplianceScore(double thresholdPercentage) {
        return complianceChecklistRepository.findAll().stream()
            .filter(checklist -> ComplianceChecklistUtils.calculateCompliancePercentage(checklist.getChecklistItems()) < thresholdPercentage)
            .map(checklistMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ComplianceChecklistDTO addChecklistItem(Long checklistId, ChecklistItemDTO newItemDTO) {
        ComplianceChecklist checklist = complianceChecklistRepository.findById(checklistId)
            .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + checklistId));
        
        ChecklistItem newItem = checklistMapper.toChecklistItemEntity(newItemDTO);
        checklist.getChecklistItems().add(newItem);
        
        updateOverallStatus(checklist);
        
        ComplianceChecklist updatedChecklist = complianceChecklistRepository.save(checklist);
        return checklistMapper.toDTO(updatedChecklist);
    }

    @Override
    @Transactional
    public ComplianceChecklistDTO removeChecklistItem(Long checklistId, Long itemId) {
        ComplianceChecklist checklist = complianceChecklistRepository.findById(checklistId)
            .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + checklistId));
        
        checklist.getChecklistItems().removeIf(item -> item.getId().equals(itemId));
        
        updateOverallStatus(checklist);
        
        ComplianceChecklist updatedChecklist = complianceChecklistRepository.save(checklist);
        return checklistMapper.toDTO(updatedChecklist);
    }

    @Override
    @Transactional(readOnly = true)
    public long countChecklistsByDateRange(LocalDate startDate, LocalDate endDate) {
        return complianceChecklistRepository.findByReviewDateBetween(startDate, endDate).size();
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateAverageComplianceScore(LocalDate startDate, LocalDate endDate) {
        List<ComplianceChecklist> checklists = complianceChecklistRepository.findByReviewDateBetween(startDate, endDate);
        
        return checklists.stream()
            .mapToDouble(checklist -> ComplianceChecklistUtils.calculateCompliancePercentage(checklist.getChecklistItems()))
            .average()
            .orElse(0.0);
    }

    // Helper method to update overall status
    private void updateOverallStatus(ComplianceChecklist checklist) {
        if (checklist.getChecklistItems() == null || checklist.getChecklistItems().isEmpty()) {
            checklist.setOverallStatus(ComplianceChecklist.ComplianceStatus.NON_COMPLIANT);
            return;
        }

        long totalItems = checklist.getChecklistItems().size();
        long passedItems = checklist.getChecklistItems().stream()
            .filter(ChecklistItem::getPassed)
            .count();

        if (passedItems == totalItems) {
            checklist.setOverallStatus(ComplianceChecklist.ComplianceStatus.COMPLIANT);
        } else if (passedItems > 0) {
            checklist.setOverallStatus(ComplianceChecklist.ComplianceStatus.PARTIALLY_COMPLIANT);
        } else {
            checklist.setOverallStatus(ComplianceChecklist.ComplianceStatus.NON_COMPLIANT);
        }
    }
}