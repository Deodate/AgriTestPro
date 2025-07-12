// // File: src/main/java/com/AgriTest/dto/ComplianceChecklistDTO.java
// package com.AgriTest.dto;

// import com.AgriTest.model.ComplianceChecklist;
// import lombok.Data;
// import lombok.Builder;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;

// import java.time.LocalDate;
// import java.util.List;

// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class ComplianceChecklistDTO {
//     private Long id;
//     private Long productId;
//     private String reviewerName;
//     private LocalDate reviewDate;
//     private List<ChecklistItemDTO> checklistItems;
//     private String overallComments;
//     private ComplianceChecklist.ComplianceStatus overallStatus;
// }