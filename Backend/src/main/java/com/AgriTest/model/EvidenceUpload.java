package com.AgriTest.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "evidence_upload")
public class EvidenceUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "taken_by")
    private String takenBy;

    @Column(name = "date_captured")
    @Temporal(TemporalType.DATE)
    private Date dateCaptured;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public Date getDateCaptured() {
        return dateCaptured;
    }

    public void setDateCaptured(Date dateCaptured) {
        this.dateCaptured = dateCaptured;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Consider adding a no-arg constructor if needed by JPA
    public EvidenceUpload() {
    }

    // Consider adding a constructor for easier object creation if needed
    public EvidenceUpload(String testName, String mediaType, String fileName, String description, String takenBy, Date dateCaptured) {
        this.testName = testName;
        this.mediaType = mediaType;
        this.fileName = fileName;
        this.description = description;
        this.takenBy = takenBy;
        this.dateCaptured = dateCaptured;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
} 