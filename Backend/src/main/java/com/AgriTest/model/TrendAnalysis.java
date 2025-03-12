// File: src/main/java/com/AgriTest/model/TrendAnalysis.java
package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trend_analyses")
public class TrendAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String analysisType;

    @Column(nullable = false)
    private String dataSource;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column
    private String entityType;

    @Column
    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String analysisResults;

    @Column(columnDefinition = "TEXT")
    private String forecastResults;

    @Column
    private Double confidenceScore;

    @Enumerated(EnumType.STRING)
    private AnalysisPeriod period;

    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    public enum AnalysisPeriod {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
    }

    public enum AnalysisStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED
    }
}