package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "compliance_checklists")
public class ComplianceChecklist extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "reviewer_name", nullable = false)
    private String reviewerName;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "checklist_id")
    private List<ChecklistItem> checklistItems;

    @Column(name = "overall_comments")
    private String overallComments;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_status", nullable = false)
    private ComplianceStatus overallStatus;

    public enum ComplianceStatus {
        COMPLIANT,
        PARTIALLY_COMPLIANT,
        NON_COMPLIANT
    }
}