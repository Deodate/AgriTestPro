package com.AgriTest.mapper;

import com.AgriTest.dto.ComplianceChecklistDTO;
import com.AgriTest.dto.ChecklistItemDTO;
import com.AgriTest.model.ComplianceChecklist;
import com.AgriTest.model.ChecklistItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComplianceChecklistMapper {

    public ComplianceChecklist toEntity(ComplianceChecklistDTO dto) {
        if (dto == null) {
            return null;
        }

        List<ChecklistItem> items = dto.getChecklistItems().stream()
            .map(this::toChecklistItemEntity)
            .collect(Collectors.toList());

        return ComplianceChecklist.builder()
            .id(dto.getId())
            .productId(dto.getProductId())
            .reviewerName(dto.getReviewerName())
            .reviewDate(dto.getReviewDate())
            .checklistItems(items)
            .overallComments(dto.getOverallComments())
            .overallStatus(dto.getOverallStatus())
            .build();
    }

    public ComplianceChecklistDTO toDTO(ComplianceChecklist entity) {
        if (entity == null) {
            return null;
        }

        List<ChecklistItemDTO> itemDTOs = entity.getChecklistItems().stream()
            .map(this::toChecklistItemDTO)
            .collect(Collectors.toList());

        return ComplianceChecklistDTO.builder()
            .id(entity.getId())
            .productId(entity.getProductId())
            .reviewerName(entity.getReviewerName())
            .reviewDate(entity.getReviewDate())
            .checklistItems(itemDTOs)
            .overallComments(entity.getOverallComments())
            .overallStatus(entity.getOverallStatus())
            .build();
    }

    public ChecklistItem toChecklistItemEntity(ChecklistItemDTO dto) {
        if (dto == null) {
            return null;
        }

        return ChecklistItem.builder()
            .id(dto.getId())
            .itemDescription(dto.getItemDescription())
            .passed(dto.getPassed())
            .comments(dto.getComments())
            .build();
    }

    public ChecklistItemDTO toChecklistItemDTO(ChecklistItem entity) {
        if (entity == null) {
            return null;
        }

        return ChecklistItemDTO.builder()
            .id(entity.getId())
            .itemDescription(entity.getItemDescription())
            .passed(entity.getPassed())
            .comments(entity.getComments())
            .build();
    }
}