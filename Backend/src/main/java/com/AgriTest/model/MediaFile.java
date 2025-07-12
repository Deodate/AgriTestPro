package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_report_id")
    private QualityIncidentReport incidentReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_activity_id")
    private FieldActivity fieldActivity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trial_phase_id")
    private TestCaseTrialPhase testCaseTrialPhase;
    
    @Column(name = "uploaded_by")
    private Long uploadedBy;
    
    @Column(name = "association_type")
    private String associationType;
    
    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    public void setFieldActivity(FieldActivity fieldActivity) {
        this.fieldActivity = fieldActivity;
    }

    public void setTestCaseTrialPhase(TestCaseTrialPhase testCaseTrialPhase) {
        this.testCaseTrialPhase = testCaseTrialPhase;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }
}