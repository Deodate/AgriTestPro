package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dataSource;  // e.g., "soil_moisture", "crop_yield", "test_results"

    @Column(nullable = false)
    private LocalDate dataDate;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = true)
    private Long entityId;  // Reference to a field, crop, test, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType;  // FIELD, CROP, TEST_CASE, etc.

    @Column(nullable = true)
    private String metricName;  // Optional additional metric categorization

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}