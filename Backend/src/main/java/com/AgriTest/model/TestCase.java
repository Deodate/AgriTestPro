package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "test_cases")
@Data
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "test_name", nullable = false)
    private String testName;
    
    @Column(name = "test_description", columnDefinition = "TEXT")
    private String testDescription;
    
    @Column(name = "test_objectives", columnDefinition = "TEXT")
    private String testObjectives;
    
    @Column(name = "product_type", nullable = false)
    private String productType;
    
    @Column(name = "product_batch_number", nullable = false)
    private String productBatchNumber;
    
    @Column(name = "testing_location", nullable = false)
    private String testingLocation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_worker_id", nullable = false)
    private User assignedWorker;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "created_by")
    private Long createdBy;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestPhase> phases = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}