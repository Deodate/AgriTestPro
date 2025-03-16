package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long uploadedBy;
    private Long testResultId;
    private Long incidentReportId;
}