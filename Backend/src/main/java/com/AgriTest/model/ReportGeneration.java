package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
   
@Entity
@Table(name = "report_generations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportGeneration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;
    
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    
    @Column(name = "product_type")
    private String productType;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "status")
    private String status;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "report_format", nullable = false)
    private ReportFormat reportFormat;
    
    @Column(name = "email_report", nullable = false)
    private Boolean emailReport;
    
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }
    
    // Enum for Report Types
    public enum ReportType {
        INVENTORY,
        TESTING_RESULTS,
        COMPLIANCE
    }
    
    // Enum for Report Formats
    public enum ReportFormat {
        PDF,
        EXCEL,
        CSV
    }
}