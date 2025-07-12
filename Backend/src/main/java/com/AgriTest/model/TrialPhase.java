package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
<<<<<<< HEAD
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
=======
import java.time.LocalDateTime;
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589

@Data
@Entity
@Table(name = "trial_phases")
public class TrialPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCase testCase;

    @Column(name = "phase_name", nullable = false)
    private String phaseName;

    @Column(name = "date_of_phase", nullable = false)
    private LocalDateTime dateOfPhase;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "test_data_entry", columnDefinition = "TEXT")
    private String testDataEntry;

    @Column(name = "weather_temperature")
    private Double weatherTemperature;

    @Column(name = "weather_humidity")
    private Double weatherHumidity;

    @Column(name = "weather_rainfall")
    private Double weatherRainfall;

    @Column(name = "additional_comments", columnDefinition = "TEXT")
    private String additionalComments;

<<<<<<< HEAD
    @Column(name = "status", nullable = false)
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trial_phase_id")
    private List<FileAttachment> attachments = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
=======
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
>>>>>>> b4bf426c868bf8a31ce2bf61cb39fc9aed839589

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 