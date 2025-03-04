package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "forecast_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "forecast_model_id", nullable = false)
    private ForecastModel forecastModel;

    @Column(nullable = false)
    private LocalDate forecastDate;

    @Column(nullable = false)
    private Double forecastValue;

    @Column(nullable = true)
    private Double lowerBound;

    @Column(nullable = true)
    private Double upperBound;

    @Column(nullable = true)
    private Double confidenceLevel;

    @CreationTimestamp
    private LocalDateTime createdAt;
}