package com.AgriTest.mapper;

import com.AgriTest.dto.ComplianceChecklistRequest;
import com.AgriTest.dto.ComplianceChecklistResponse;
import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.model.ComplianceChecklistItem;
import com.AgriTest.model.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ComplianceChecklistMapper {
    
    public ComplianceChecklist toEntity(ComplianceChecklistRequest request, Product product) {
        ComplianceChecklist checklist = new ComplianceChecklist();
        checklist.setProduct(product);
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
        
        Map<String, Boolean> checklistItemsMap = new HashMap<>();
        if (checklist.getChecklistItems() != null) {
            checklistItemsMap = checklist.getChecklistItems().stream()
                    .collect(Collectors.toMap(
                            ComplianceChecklistItem::getItemName, 
                            ComplianceChecklistItem::isChecked,
                            (existing, replacement) -> existing
                    ));
        }
        response.setChecklistItems(checklistItemsMap);
        
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
        checklist.setReviewerName(request.getReviewerName());
        checklist.setReviewDate(request.getReviewDate());
        checklist.setComments(request.getComments());
        // Overall status will be recalculated in @PreUpdate
    }
}