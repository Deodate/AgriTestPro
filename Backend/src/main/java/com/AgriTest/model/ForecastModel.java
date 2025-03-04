package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "forecast_models")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String dataSource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ForecastType forecastType;

    @Column(nullable = false)
    private Integer forecastHorizon;  // Number of periods to forecast

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long createdBy;

    // Parameters for the forecasting model as JSON
    @Column(columnDefinition = "TEXT")
    private String modelParameters;
    
    // Training status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModelStatus status = ModelStatus.UNTRAINED;
    
    // Last training time
    private LocalDateTime lastTrainedAt;
}