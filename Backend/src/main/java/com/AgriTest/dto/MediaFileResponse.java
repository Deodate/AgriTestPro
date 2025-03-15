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
    private Long testResultId;
    private Long incidentReportId;
    private String fileName;
    private String fileType;
    private String filePath;
    private Long uploadedBy;
    private LocalDateTime uploadedAt;
}