package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFileResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String fileDownloadUri;  // This is what we need to match in our code
    private long size;
    private String description;
    private LocalDateTime uploadedAt;
    private Long uploadedBy;
    private String associationType;
    
    // Entity relationships represented as IDs
    private Long testResultId;
    private Long incidentReportId;
    private Long announcementId;
    private Long expenseId;
}