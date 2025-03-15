package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_analyses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceAnalysis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "test_case_id", nullable = false)
    private String testCaseId;
    
    @Column(name = "trial_results", columnDefinition = "TEXT")
    private String trialResults;
    
    @Column(name = "seasonal_performance")
    private String seasonalPerformanceMetrics;
    
    @Column(name = "effectiveness_rating")
    private Double effectivenessRating;
    
    @Column(name = "previous_season_comparison", columnDefinition = "TEXT")
    private String previousSeasonComparison;
    
    @Column(name = "trial_date")
    private LocalDate trialDate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}