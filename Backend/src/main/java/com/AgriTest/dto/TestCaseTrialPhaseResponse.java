// File: src/main/java/com/AgriTest/dto/TestCaseTrialPhaseResponse.java
package com.AgriTest.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestCaseTrialPhaseResponse {
    private Long id;
    private Long testCaseId;
    private String phaseName;
    private LocalDate phaseDate;
    private String testName;
    private String observations;
    private String testDataEntry;
    private WeatherDataDto weatherData;
    private String additionalComments;
    private List<FileAttachmentDto> attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class WeatherDataDto {
        private Double temperature;
        private Double humidity;
        private Double rainfall;
    }

    @Data
    public static class FileAttachmentDto {
        private Long id;
        private String fileName;
        private String fileType;
        private String filePath;
    }
}