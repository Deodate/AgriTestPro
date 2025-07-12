package com.AgriTest.service.impl;

import com.AgriTest.dto.ComplianceChecklistRequest;
import com.AgriTest.dto.ComplianceChecklistResponse;
import com.AgriTest.exception.ResourceNotFoundException;
import com.AgriTest.mapper.ComplianceChecklistMapper;
import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.model.ComplianceChecklistItem;
import com.AgriTest.model.Product;
import com.AgriTest.repository.ComplianceChecklistRepository;
import com.AgriTest.repository.ComplianceChecklistItemRepository;
import com.AgriTest.repository.ProductRepository;
import com.AgriTest.service.ComplianceChecklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComplianceChecklistServiceImpl implements ComplianceChecklistService {
    
    private static final Logger logger = LoggerFactory.getLogger(ComplianceChecklistServiceImpl.class);
    
    @Autowired
    private ComplianceChecklistRepository complianceChecklistRepository;
    
    @Autowired
    private ComplianceChecklistItemRepository listItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ComplianceChecklistMapper complianceChecklistMapper;
    
    @Override
    @Transactional
    public ComplianceChecklistResponse createComplianceChecklist(ComplianceChecklistRequest request) {
        logger.info("Attempting to create new compliance checklist for product name: {}", request.getProductName());
        
        // Find the product by name
        List<Product> products = productRepository.findByName(request.getProductName());
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with name: " + request.getProductName());
        }
        Product product = products.get(0); // Take the first product if multiple exist
        if (products.size() > 1) {
            logger.warn("Multiple products found with name '{}'. Using the first one found with ID: {}", request.getProductName(), product.getId());
        }
        
        // Create checklist entity from request data (excluding items initially)
        ComplianceChecklist checklist = new ComplianceChecklist();
        checklist.setProduct(product);
        checklist.setReviewerName(request.getReviewerName());
        checklist.setReviewDate(request.getReviewDate());
        checklist.setComments(request.getComments());
        // createdAt and updatedAt are handled by @PrePersist/@PreUpdate
        // overallStatus is calculated in @PrePersist
        
        // Save the checklist first to get its ID assigned
        ComplianceChecklist savedChecklist = complianceChecklistRepository.save(checklist);

        // Add checklist items from the request map to the saved checklist
        if (request.getChecklistItems() != null) {
            request.getChecklistItems().forEach((itemName, isChecked) -> {
                ComplianceChecklistItem item = new ComplianceChecklistItem();
                item.setItemName(itemName);
                item.setChecked(isChecked);
                // Associate the item with the saved checklist
                item.setComplianceChecklist(savedChecklist);
                // Add to the checklist's list (optional but good practice for consistency)
                savedChecklist.getChecklistItems().add(item);
            });
        }
        
        // The items should now be saved automatically due to CascadeType.ALL on the OneToMany relationship
        // if they were added to the savedChecklist's list. To be explicit, we can also save items manually if needed,
        // but let's first rely on cascade after ensuring the parent has an ID.
        // complianceChecklistRepository.save(savedChecklist); // Re-saving might not be necessary but ensures cascade

        logger.info("Successfully created compliance checklist with ID: {}", savedChecklist.getId());
        
        // Return the DTO
        return complianceChecklistMapper.toDto(savedChecklist);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getAllComplianceChecklists() {
        logger.info("Fetching all compliance checklists");
        List<ComplianceChecklist> checklists = complianceChecklistRepository.findAll();
        logger.info("Found {} compliance checklists", checklists.size());
        return checklists.stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ComplianceChecklistResponse getComplianceChecklistById(Long id) {
        logger.info("Fetching compliance checklist with ID: {}", id);
        ComplianceChecklist checklist = complianceChecklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        
        logger.info("Successfully fetched compliance checklist with ID: {}", id);
        return complianceChecklistMapper.toDto(checklist);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getComplianceChecklistsByProductId(Long productId) {
        logger.info("Fetching compliance checklists for product ID: {}", productId);
        // Verify the product exists
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        
        List<ComplianceChecklist> checklists = complianceChecklistRepository.findByProductId(productId);
        logger.info("Found {} compliance checklists for product ID: {}", checklists.size(), productId);
        return checklists.stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getComplianceChecklistsByReviewerName(String reviewerName) {
        logger.info("Fetching compliance checklists by reviewer name: {}", reviewerName);
        List<ComplianceChecklist> checklists = complianceChecklistRepository.findByReviewerNameContainingIgnoreCase(reviewerName);
        logger.info("Found {} compliance checklists by reviewer name: {}", checklists.size(), reviewerName);
        return checklists.stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getComplianceChecklistsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching compliance checklists between {} and {}", startDate, endDate);
        // Validate input dates
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both start and end dates must be provided");
        }
        
        // Ensure start date is before or equal to end date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        
        // Retrieve checklists within the date range
        List<ComplianceChecklist> checklists = complianceChecklistRepository.findByReviewDateBetween(startDate, endDate);
        logger.info("Found {} compliance checklists between {} and {}", checklists.size(), startDate, endDate);
        return checklists.stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ComplianceChecklistResponse> getMostRecentChecklists(int limit) {
        logger.info("Fetching {} most recent compliance checklists", limit);
        // Validate limit
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be a positive number");
        }
        
        // Retrieve most recent checklists
        List<ComplianceChecklist> checklists = complianceChecklistRepository.findMostRecentChecklists(limit);
        logger.info("Found {} most recent compliance checklists", checklists.size());
        return checklists.stream()
                .map(complianceChecklistMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ComplianceChecklistResponse updateComplianceChecklist(Long id, ComplianceChecklistRequest request) {
        logger.info("Updating compliance checklist with ID: {}", id);
        
        // Find the existing checklist
        ComplianceChecklist existingChecklist = complianceChecklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        
        // Find the product by name
        List<Product> products = productRepository.findByName(request.getProductName());
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with name: " + request.getProductName());
        }
        Product product = products.get(0); // Take the first product if multiple exist
        if (products.size() > 1) {
            logger.warn("Multiple products found with name '{}'. Using the first one found with ID: {}", request.getProductName(), product.getId());
        }
        
        // Update the main checklist fields
        existingChecklist.setProduct(product);
        existingChecklist.setReviewerName(request.getReviewerName());
        existingChecklist.setReviewDate(request.getReviewDate());
        existingChecklist.setComments(request.getComments());
        
        // Clear existing items and add new ones from the request
        existingChecklist.getChecklistItems().clear(); // Remove old items
        if (request.getChecklistItems() != null) {
            request.getChecklistItems().forEach((itemName, isChecked) -> {
                ComplianceChecklistItem item = new ComplianceChecklistItem();
                item.setItemName(itemName);
                item.setChecked(isChecked);
                existingChecklist.addChecklistItem(item); // Use helper method
            });
        }
        // overallStatus will be recalculated in @PreUpdate
        
        // Save the updated checklist (this will cascade save/remove items)
        ComplianceChecklist updatedChecklist = complianceChecklistRepository.save(existingChecklist);
        
        logger.info("Successfully updated compliance checklist with ID: {}", updatedChecklist.getId());
        
        // Return the updated DTO
        return complianceChecklistMapper.toDto(updatedChecklist);
    }
    
    @Override
    @Transactional
    public void deleteComplianceChecklist(Long id) {
        logger.info("Deleting compliance checklist with ID: {}", id);
        // Find the checklist
        ComplianceChecklist checklist = complianceChecklistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance checklist not found with id: " + id));
        
        // Deleting the checklist will cascade delete the items due to orphanRemoval=true
        complianceChecklistRepository.delete(checklist);
        
        logger.info("Successfully deleted compliance checklist with ID: {}", id);
    }
}