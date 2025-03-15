package com.AgriTest.mapper;

import com.AgriTest.dto.ComplianceChecklistRequest;
import com.AgriTest.dto.ComplianceChecklistResponse;
import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ComplianceChecklistMapper {
    
    public ComplianceChecklist toEntity(ComplianceChecklistRequest request, Product product) {
        ComplianceChecklist checklist = new ComplianceChecklist();
        checklist.setProduct(product);
        checklist.setChecklistItems(request.getChecklistItems());
        checklist.setReviewerName(request.getReviewerName());
        checklist.setReviewDate(request.getReviewDate());
        checklist.setComments(request.getComments());
        // Overall status will be calculated in @PrePersist
        return checklist;
    }
    
    public ComplianceChecklistResponse toDto(ComplianceChecklist checklist) {
        ComplianceChecklistResponse response = new ComplianceChecklistResponse();
        response.setId(checklist.getId());
        response.setProductId(checklist.getProduct().getId());
        response.setProductName(checklist.getProduct().getName());
        response.setChecklistItems(checklist.getChecklistItems());
        response.setReviewerName(checklist.getReviewerName());
        response.setReviewDate(checklist.getReviewDate());
        response.setComments(checklist.getComments());
        response.setOverallStatus(checklist.getOverallStatus());
        response.setCreatedAt(checklist.getCreatedAt());
        response.setUpdatedAt(checklist.getUpdatedAt());
        return response;
    }
    
    public void updateEntityFromRequest(ComplianceChecklist checklist, ComplianceChecklistRequest request, Product product) {
        checklist.setProduct(product);
        checklist.setChecklistItems(request.getChecklistItems());
        checklist.setReviewerName(request.getReviewerName());
        checklist.setReviewDate(request.getReviewDate());
        checklist.setComments(request.getComments());
        // Overall status will be recalculated in @PreUpdate
    }
}