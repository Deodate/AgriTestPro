package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "test_results")
@Data
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_phase_id")
    private TestPhase testPhase;

    private String parameterName;
    private String value;
    private String unit;
    private String notes;
    private Long recordedBy;
    private LocalDateTime recordedAt;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "trial_phase", nullable = false)
    private String trialPhase;

    @Column(name = "growth_rate")
    private Double growthRate;

    @Column(name = "yield")
    private Double yield;

    @Column(name = "pest_resistance")
    private Double pestResistance;

    @Column(name = "final_verdict", nullable = false)
    private String finalVerdict; // PASS/FAIL

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "approved_by", nullable = false)
    private String approvedBy;

    @Column(name = "date_of_approval", nullable = false)
    private LocalDateTime dateOfApproval;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> mediaFiles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
