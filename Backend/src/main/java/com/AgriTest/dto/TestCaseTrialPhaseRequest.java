// File: src/main/java/com/AgriTest/dto/TestCaseTrialPhaseRequest.java
package com.AgriTest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class TestCaseTrialPhaseRequest {
    @NotNull(message = "Test Case ID is required")
    private Long testCaseId;

    @NotBlank(message = "Phase Name is required")
    private String phaseName;

    @NotNull(message = "Phase Date is required")
    private LocalDate phaseDate;

    private List<String> observations;
    private List<String> testDataEntries;

    private WeatherDataDto weatherData;
    private String additionalComments;

    private List<MultipartFile> attachments;

    @Data
    public static class WeatherDataDto {
        private Double temperature;
        private Double humidity;
        private Double rainfall;
    }
}