package com.AgriTest.service.impl;

import com.AgriTest.dto.ComplianceChecklistRequest;
import com.AgriTest.dto.ComplianceChecklistResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.ComplianceChecklistMapper;
import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.model.Product;
import com.AgriTest.repository.ComplianceChecklistRepository;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.service.ComplianceChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplianceChecklistServiceImpl implements ComplianceChecklistService {
    
    @Autowired
    private ComplianceChecklistRepository complianceChecklistRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ComplianceChecklistMapper complianceChecklistMapper;
    
    @Override
    @Transactional
    public ComplianceChecklistResponse createComplianceChecklist(ComplianceChecklistRequest request) {
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Create checklist entity
        ComplianceChecklist checklist = complianceChecklistMapper.toEntity(request, product);
        
        // Save the checklist
        ComplianceChecklist savedChecklist = complianceChecklistRepository.save(checklist);
        
        // Return the DTO
        return complianceChecklistMapper.toDto(savedChecklist);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getAllComplianceChecklists() {
        return complianceChecklistRepository.findAll().stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ComplianceChecklistResponse getComplianceChecklistById(Long id) {
        ComplianceChecklist checklist = complianceChecklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        
        return complianceChecklistMapper.toDto(checklist);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getComplianceChecklistsByProductId(Long productId) {
        // Verify the product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        return complianceChecklistRepository.findByProductId(productId).stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getComplianceChecklistsByReviewerName(String reviewerName) {
        return complianceChecklistRepository.findByReviewerNameContainingIgnoreCase(reviewerName).stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getComplianceChecklistsByDateRange(LocalDate startDate, LocalDate endDate) {
        // Validate input dates
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both start and end dates must be provided");
        }
        
        // Ensure start date is before or equal to end date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        // Retrieve checklists within the date range
        return complianceChecklistRepository.findByReviewDateBetween(startDate, endDate).stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getMostRecentChecklists(int limit) {
        // Validate limit
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }
        
        // Retrieve most recent checklists
        return complianceChecklistRepository.findMostRecentChecklists(limit).stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ComplianceChecklistResponse updateComplianceChecklist(Long id, ComplianceChecklistRequest request) {
        // Find the existing checklist
        ComplianceChecklist existingChecklist = complianceChecklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        
        // Find the product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        
        // Update the existing checklist
        complianceChecklistMapper.updateEntityFromRequest(existingChecklist, request, product);
        
        // Save the updated checklist
        ComplianceChecklist updatedChecklist = complianceChecklistRepository.save(existingChecklist);
        
        // Return the updated DTO
        return complianceChecklistMapper.toDto(updatedChecklist);
    }
    
    @Override
    @Transactional
    public void deleteComplianceChecklist(Long id) {
        // Find the checklist
        ComplianceChecklist checklist = complianceChecklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        
        // Delete the checklist
        complianceChecklistRepository.delete(checklist);
    }
}