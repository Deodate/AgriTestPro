// File: src/main/java/com/AgriTest/model/TestCaseTrialPhase.java
package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "test_case_trial_phases")
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseTrialPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_case_id", nullable = false)
    private Long testCaseId;

    @Column(name = "phase_name", nullable = false)
    private String phaseName;

    @Column(name = "phase_date", nullable = false)
    private LocalDate phaseDate;

    @ElementCollection
    @CollectionTable(name = "trial_phase_observations", joinColumns = @JoinColumn(name = "trial_phase_id"))
    @Column(name = "observation")
    private List<String> observations;

    @ElementCollection
    @CollectionTable(name = "trial_phase_test_data", joinColumns = @JoinColumn(name = "trial_phase_id"))
    @Column(name = "data_entry")
    private List<String> testDataEntries;

    @Embedded
    private WeatherData weatherData;

    @Column(name = "additional_comments", length = 1000)
    private String additionalComments;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trial_phase_id")
    private List<FileAttachment> attachments;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherData {
        @Column(name = "temperature")
        private Double temperature;

        @Column(name = "humidity")
        private Double humidity;

        @Column(name = "rainfall")
        private Double rainfall;
    }
}