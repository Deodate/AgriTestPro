package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "compliance_checklists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceChecklist {
    
    public enum ComplianceStatus {
        COMPLIANT,
        PARTIALLY_COMPLIANT,
        NON_COMPLIANT
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ElementCollection
    @CollectionTable(name = "compliance_checklist_items", 
                    joinColumns = @JoinColumn(name = "checklist_id"))
    @MapKeyColumn(name = "item_name")
    @Column(name = "item_status")
    private Map<String, Boolean> checklistItems = new HashMap<>();
    
    @Column(name = "reviewer_name", nullable = false)
    private String reviewerName;
    
    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;
    
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "overall_status")
    private ComplianceStatus overallStatus;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        calculateOverallStatus();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateOverallStatus();
    }
    
    private void calculateOverallStatus() {
        if (checklistItems == null || checklistItems.isEmpty()) {
            this.overallStatus = ComplianceStatus.NON_COMPLIANT;
            return;
        }
        
        long totalItems = checklistItems.size();
        long passedItems = checklistItems.values().stream()
                .filter(Boolean::booleanValue)
                .count();
        
        if (passedItems == totalItems) {
            this.overallStatus = ComplianceStatus.COMPLIANT;
        } else if (passedItems > 0) {
            this.overallStatus = ComplianceStatus.PARTIALLY_COMPLIANT;
        } else {
            this.overallStatus = ComplianceStatus.NON_COMPLIANT;
        }
    }
}