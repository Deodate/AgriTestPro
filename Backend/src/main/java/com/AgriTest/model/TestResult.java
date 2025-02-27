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
    @JoinColumn(name = "test_phase_id", nullable = false)
    private TestPhase testPhase;

    @Column(name = "parameter_name", nullable = false)
    private String parameterName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;

    private String unit;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "recorded_by")
    private Long recordedBy;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> mediaFiles = new ArrayList<>();
}
