package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaAttachment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "incident_report_id", nullable = false)
    private QualityIncidentReport incidentReport;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_type", nullable = false)
    private String fileType;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}