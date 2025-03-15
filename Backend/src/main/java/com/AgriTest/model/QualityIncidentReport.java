package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quality_incident_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QualityIncidentReport {
    
    public enum IncidentStatus {
        OPEN,
        IN_PROGRESS,
        RESOLVED,
        CLOSED
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "incident_id", nullable = false, unique = true)
    private String incidentId;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;
    
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @OneToMany(mappedBy = "incidentReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> mediaFiles = new ArrayList<>();
    
    @Column(name = "corrective_actions", columnDefinition = "TEXT")
    private String correctiveActions;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IncidentStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}