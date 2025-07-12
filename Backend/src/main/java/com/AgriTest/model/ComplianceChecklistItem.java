package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "compliance_checklist_items")
public class ComplianceChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compliance_checklist_id", nullable = false)
    private ComplianceChecklist complianceChecklist; // Foreign key to ComplianceChecklist

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;
} 