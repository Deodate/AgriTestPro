package com.AgriTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponse {
    private Long id;
    private Long testPhaseId;
    private String parameterName;
    private String value;
    private String unit;
    private String notes;
    private Long recordedBy;
    private LocalDateTime recordedAt;
    private List<MediaFileResponse> mediaFiles;
}