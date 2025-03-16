package com.AgriTest.dto;

import com.AgriTest.model.QualityIncidentReport;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

public class QualityIncidentReportDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Incident Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime incidentDate;

    @NotNull(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @Size(max = 1000, message = "Corrective actions must be less than 1000 characters")
    private String correctiveActions;

    private QualityIncidentReport.IncidentStatus status;

    private List<MultipartFile> mediaFiles;

    // Constructors
    public QualityIncidentReportDTO() {
        // Default constructor
        this.status = QualityIncidentReport.IncidentStatus.OPEN;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDateTime getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDateTime incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCorrectiveActions() {
        return correctiveActions;
    }

    public void setCorrectiveActions(String correctiveActions) {
        this.correctiveActions = correctiveActions;
    }

    public QualityIncidentReport.IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(QualityIncidentReport.IncidentStatus status) {
        this.status = status != null ? status : QualityIncidentReport.IncidentStatus.OPEN;
    }

    public List<MultipartFile> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<MultipartFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    // Optional: toString method for logging and debugging
    @Override
    public String toString() {
        return "QualityIncidentReportDTO{" +
                "productId=" + productId +
                ", incidentDate=" + incidentDate +
                ", description='" + description + '\'' +
                ", correctiveActions='" + correctiveActions + '\'' +
                ", status=" + status +
                ", mediaFiles=" + (mediaFiles != null ? mediaFiles.size() : "0") +
                '}';
    }
}