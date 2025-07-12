package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    
    @OneToMany(mappedBy = "complianceChecklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComplianceChecklistItem> checklistItems = new ArrayList<>();
    
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
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
        long passedItems = checklistItems.stream()
                .filter(ComplianceChecklistItem::isChecked)
                .count();
        
        if (passedItems == totalItems) {
            this.overallStatus = ComplianceStatus.COMPLIANT;
        } else if (passedItems > 0) {
            this.overallStatus = ComplianceStatus.PARTIALLY_COMPLIANT;
        } else {
            this.overallStatus = ComplianceStatus.NON_COMPLIANT;
        }
    }

    public void addChecklistItem(ComplianceChecklistItem item) {
        checklistItems.add(item);
        item.setComplianceChecklist(this);
    }

    public void removeChecklistItem(ComplianceChecklistItem item) {
        checklistItems.remove(item);
        item.setComplianceChecklist(null);
    }
}