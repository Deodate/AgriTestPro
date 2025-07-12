package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

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