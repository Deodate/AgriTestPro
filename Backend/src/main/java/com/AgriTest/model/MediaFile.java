package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaFile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_type")
    private String fileType;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "test_result_id", nullable = true)
    private TestResult testResult;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "incident_report_id", nullable = true)
    private QualityIncidentReport incidentReport;

    @ManyToOne(optional = true)
    @JoinColumn(name = "announcement_id", nullable = true)
    private Announcement announcement;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "expense_id", nullable = true)
    private Expense expense;
    
    // Add this new relationship for Field Activity
    @ManyToOne(optional = true)
    @JoinColumn(name = "field_activity_id", nullable = true)
    private FieldActivity fieldActivity;
    
    @Column(name = "uploaded_by")
    private Long uploadedBy;
    
    @Column(name = "association_type")
    private String associationType;
    
    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}